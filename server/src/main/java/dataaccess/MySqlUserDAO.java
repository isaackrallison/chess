package dataaccess;

import model.UserData;
import java.sql.*;

public class MySqlUserDAO implements UserDAO {
    public MySqlUserDAO() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        String sql = "SELECT * FROM users WHERE username = ?";
        UserData userData = null;

        try (var conn = DatabaseManager.getConnection();
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (var rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    userData = new UserData(
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("email")
                    );
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Unable to retrieve user: " + e.getMessage());
        }

        return userData;
    }

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";

        try (var conn = DatabaseManager.getConnection();
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userData.username());
            pstmt.setString(2, userData.password());
            pstmt.setString(3, userData.email());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Unable to create user: " + e.getMessage());
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


