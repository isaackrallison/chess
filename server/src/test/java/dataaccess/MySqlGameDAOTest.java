package dataaccess;

import chess.ChessGame;
import dataaccess.exceptions.DataAccessException;
import model.GameData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class MySqlGameDAOTest {
    private MySqlGameDAO gameDAO;

    @BeforeEach
    public void setUp() throws DataAccessException {
        gameDAO = new MySqlGameDAO();
        gameDAO.clearGames(); // Clear the games table before each test
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        gameDAO.clearGames(); // Clean up after tests
    }

    @Test
    public void testCreateGamePositive() throws DataAccessException {
        ChessGame game = new ChessGame(); // Assuming you have a constructor or method to create a game
        String gameName = "Test Game";
        gameDAO.createGame(1, game, gameName);

        // Verify that the game was created
        GameData createdGame = gameDAO.getAllGames().get(0);
        assertNotNull(createdGame);
        assertEquals("Test Game", createdGame.gameName());
        assertNotNull(createdGame.game());
    }

    @Test
    public void testGetAllGames() throws DataAccessException {
        ChessGame game1 = new ChessGame(); // Create a game instance
        ChessGame game2 = new ChessGame(); // Create another game instance
        gameDAO.createGame(1, game1, "Game One");
        gameDAO.createGame(2, game2, "Game Two");

        // Retrieve all games
        List<GameData> games = gameDAO.getAllGames();
        assertEquals(2, games.size());
        assertEquals("Game One", games.get(0).gameName());
        assertEquals("Game Two", games.get(1).gameName());
    }

    @Test
    public void testFindGameByIdPositive() throws DataAccessException {
        ChessGame game = new ChessGame(); // Create a game instance
        gameDAO.createGame(1, game, "Game One");

        // Retrieve the game by ID
        ChessGame retrievedGame = gameDAO.findGameById(1);
        assertNotNull(retrievedGame);
    }

    @Test
    public void testFindGameByIdNegative() throws DataAccessException {
        // Try to retrieve a game that doesn't exist
        ChessGame retrievedGame = gameDAO.findGameById(999); // Assuming this ID does not exist
        assertNull(retrievedGame);
    }

    @Test
    public void testUpdateGame() throws DataAccessException {
        ChessGame game = new ChessGame(); // Create a game instance
        gameDAO.createGame(1, game, "Game One");

        // Update the game with a player's username
        gameDAO.updateGame("WHITE", 1, "Player1");

        GameData updatedGame = gameDAO.getAllGames().get(0);
        assertEquals("Player1", updatedGame.whiteUsername());
    }

    @Test
    public void testClearGames() throws DataAccessException {
        ChessGame game = new ChessGame();
        gameDAO.createGame(1, game, "Game One");

        // Clear games
        gameDAO.clearGames();

        // Verify that no games exist
        List<GameData> games = gameDAO.getAllGames();
        assertTrue(games.isEmpty());
    }
}

