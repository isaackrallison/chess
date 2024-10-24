package service;

import model.GameData;
import chess.ChessGame;
import dataaccess.MemoryGameDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import dataaccess.UnauthorizedException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CreateGameTest {

    private final MemoryGameDAO gameDAO = new MemoryGameDAO();
    private GameService gameService;

    @Test
    public void testCreateGame() {
        // Arrange
        int gameIdNum = 1;
        ChessGame chessGame = new ChessGame(); // Assuming you have a constructor
        String gameName = "Test Game";

        // Act
        gameDAO.createGame(gameIdNum, chessGame, gameName);

        // Assert
        List<GameData> games = gameDAO.getAllGames();
        assertEquals(1, games.size(), "Game list should contain one game");

        GameData createdGame = games.get(0);
        assertEquals(gameIdNum, createdGame.gameID(), "Game ID should match");
        assertEquals(gameName, createdGame.gameName(), "Game name should match");
        Assertions.assertNull(createdGame.whiteUsername(), "White username should be null initially");
        Assertions.assertNull(createdGame.blackUsername(), "Black username should be null initially");
    }

//    @Test
//    public void testCreateGameUnauthorized() {
//        // Arrange
//        String invalidAuthToken = "invalidAuthToken"; // No token is set in the authDAO, so it is invalid
//        String gameName = "Test Game";
//
//        // Act & Assert
//        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
//            gameService.createGame(gameName, invalidAuthToken);
//        });
//
//        // Verify the exception message
//        assertEquals("Error: unauthorized", exception.getMessage());
//
//        // Verify no games were created
//        List<GameData> games = gameDAO.getAllGames();
//        assertEquals(0, games.size(), "Game list should be empty since creation failed");
//    }
}

