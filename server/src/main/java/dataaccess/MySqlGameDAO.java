package dataaccess;

import chess.ChessGame;
import model.GameData;
import chess.ChessBoard;
import chess.ChessPiece;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.*;

public class MySqlGameDAO implements GameDAO {
    private final Connection connection;

    public MySqlGameDAO() throws DataAccessException {
        try {
            String url = "jdbc:mysql://localhost:3306/chess"; // Change 'chess' to your database name
            String user = "root"; // Change as needed
            String password = "password"; // Change as needed
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new DataAccessException("Error connecting to the database");
        }
    }

    @Override
    public List<GameData> getAllGames() {
        List<GameData> games = new ArrayList<>();
        String sql = "SELECT * FROM games"; // Ensure you have a 'games' table created in your DB

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int gameId = rs.getInt("game_id");
                String gameName = rs.getString("game_name");
                ChessGame game = ChessGameAdapter.deserialize(rs.getString("game_data")); // Deserialize here
                GameData gameData = new GameData(gameId, null, null, gameName, game);
                games.add(gameData);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception properly in a production scenario
        }

        return games;
    }

    @Override
    public void createGame(int gameIdNum, ChessGame game, String gameName) {
        String sql = "INSERT INTO games (game_id, game_name, game_data) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, gameIdNum);
            pstmt.setString(2, gameName);
            pstmt.setString(3, ChessGameAdapter.serialize(game)); // Serialize the game to JSON
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception properly
        }
    }

    @Override
    public ChessGame findGameById(int gameId) {
        String sql = "SELECT game_data FROM games WHERE game_id = ?";
        ChessGame game = null;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, gameId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String gameData = rs.getString("game_data");
                    game = ChessGameAdapter.deserialize(gameData); // Deserialize the JSON string to ChessGame
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception properly
        }

        return game;
    }

    @Override
    public void updateGame(String playerColor, int gameId, String username) {
        String sql = "UPDATE games SET " + (playerColor.equalsIgnoreCase("WHITE") ? "white_username" : "black_username") + " = ? WHERE game_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setInt(2, gameId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception properly
        }
    }

    @Override
    public void clearGames() {
        String sql = "DELETE FROM games";

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception properly
        }
    }

    public static class ChessGameAdapter {

        private static final Gson gson = new GsonBuilder()
                .registerTypeAdapter(ChessPiece.class, new ChessPieceAdapter())
                .registerTypeAdapter(ChessBoard.class, new ChessBoardAdapter())
                .create();

        public static String serialize(ChessGame game) {
            return gson.toJson(game);
        }

        public static ChessGame deserialize(String json) {
            return gson.fromJson(json, ChessGame.class);
        }
    }
}



