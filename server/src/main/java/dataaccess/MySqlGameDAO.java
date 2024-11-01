package dataaccess;

import chess.ChessGame;
import dataaccess.exceptions.DataAccessException;
import model.GameData;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySqlGameDAO implements GameDAO {
    public MySqlGameDAO() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public List<GameData> getAllGames() throws DataAccessException {
        List<GameData> games = new ArrayList<>();
        String sql = "SELECT * FROM games"; // Ensure you have a 'games' table created in your DB

        try (var conn = DatabaseManager.getConnection();
             var stmt = conn.createStatement();
             var rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int gameId = rs.getInt("game_id");
                String gameName = rs.getString("game_name");
                ChessGame game = ChessGameAdapter.deserialize(rs.getString("game_data"));
                String whiteUsername = rs.getString("white_username");
                String blackUsername = rs.getString("black_username");
                GameData gameData = new GameData(gameId, whiteUsername, blackUsername, gameName, game);
                games.add(gameData);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Unable to retrieve games: " + e.getMessage());
        }

        return games;
    }

    @Override
    public void createGame(int gameIdNum, ChessGame game, String gameName) throws DataAccessException {
        String sql = "INSERT INTO games (game_id, game_name, game_data) VALUES (?, ?, ?)";

        try (var conn = DatabaseManager.getConnection();
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, gameIdNum);
            pstmt.setString(2, gameName);
            pstmt.setString(3, ChessGameAdapter.serialize(game)); // Serialize the game to JSON
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Unable to create game: " + e.getMessage());
        }
    }

    @Override
    public ChessGame findGameById(int gameId) throws DataAccessException {
        String sql = "SELECT game_data FROM games WHERE game_id = ?";
        ChessGame game = null;

        try (var conn = DatabaseManager.getConnection();
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, gameId);
            try (var rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String gameData = rs.getString("game_data");
                    game = ChessGameAdapter.deserialize(gameData); // Deserialize the JSON string to ChessGame
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Unable to find game: " + e.getMessage());
        }

        return game;
    }

    @Override
    public void updateGame(String playerColor, int gameId, String username) throws DataAccessException {
        String sql = "UPDATE games SET " + (playerColor.equalsIgnoreCase("WHITE") ? "white_username" : "black_username") + " = ? WHERE game_id = ?";

        try (var conn = DatabaseManager.getConnection();
             var pstmt = conn.prepareStatement(sql)) {

            // Check if the username already exists for the specified color
            String checkSql = "SELECT " + (playerColor.equalsIgnoreCase("WHITE") ? "white_username" : "black_username") +
                    " FROM games WHERE game_id = ?";
            try (var checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, gameId);
                try (var rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getString(1) != null) {
                        throw new IllegalStateException("Error: color already taken");
                    }
                }
            }

            pstmt.setString(1, username);
            pstmt.setInt(2, gameId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Unable to update game: " + e.getMessage());
        }
    }


    @Override
    public void clearGames() throws DataAccessException {
        String sql = "DELETE FROM games";

        try (var conn = DatabaseManager.getConnection();
             var stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("Unable to clear games: " + e.getMessage());
        }
    }

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        String createStatement = """
            CREATE TABLE IF NOT EXISTS games (
              game_id INT PRIMARY KEY,
              game_name VARCHAR(255) NOT NULL,
              game_data TEXT NOT NULL,
              white_username VARCHAR(255),
              black_username VARCHAR(255)
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



