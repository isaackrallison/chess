package dataaccess;

import model.UserData;
import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;

public class MySqlUserDAO implements UserDAO {
    public MySqlUserDAO() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public UserData getUser(String username, String password) throws DataAccessException {
        String sql = "SELECT * FROM users WHERE username = ?";
        UserData userData = null;

        try (var conn = DatabaseManager.getConnection();
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (var rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String hashedPassword = rs.getString("password"); // Retrieve the hashed password
                    // Verify the provided password against the hashed password
                    if (BCrypt.checkpw(password, hashedPassword)) {
                        userData = new UserData(
                                rs.getString("username"),
                                hashedPassword, // Keep the hashed password if needed for other purposes
                                rs.getString("email")
                        );
                    } else {
                        throw new DataAccessException("Invalid password for user: " + username);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Unable to retrieve user: " + e.getMessage());
        }

        return userData; // Returns null if user doesn't exist or password is incorrect
    }


    @Override
    public void createUser(UserData userData) throws DataAccessException {
        String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";

        // Hash the password before storing it
        String hashedPassword = BCrypt.hashpw(userData.password(), BCrypt.gensalt());

        try (var conn = DatabaseManager.getConnection();
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userData.username());
            pstmt.setString(2, hashedPassword); // Store the hashed password
            pstmt.setString(3, userData.email());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Unable to create user: " + e.getMessage());
        }
    }

    public boolean verifyUser(String username, String providedClearTextPassword) throws UnauthorizedException {
        try {
            UserData userData = getUser(username, providedClearTextPassword);
            if (userData == null) {
                return false; // User does not exist
            }
            // Compare the provided password with the stored hashed password
            return BCrypt.checkpw(providedClearTextPassword, userData.password());
        } catch (DataAccessException e){
            throw new UnauthorizedException ("Unauthorized");
        }
    }

    @Override
    public void clearUsers() throws DataAccessException {
        String sql = "DELETE FROM users";

        try (var conn = DatabaseManager.getConnection();
             var stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("Unable to clear users: " + e.getMessage());
        }
    }

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        String createStatement = """
            CREATE TABLE IF NOT EXISTS users (
              username VARCHAR(255) PRIMARY KEY,
              password VARCHAR(255) NOT NULL,
              email VARCHAR(255) NOT NULL
            )
        """;

        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(createStatement)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Unable to configure database: " + e.getMessage());
        }
    }
}


