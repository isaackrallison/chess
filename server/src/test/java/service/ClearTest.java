package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClearTest {

    private UserDAO userDAO;
    private AuthDAO authDAO;
    private GameDAO gameDAO;
    private GameService gameService;

    @BeforeEach
    public void setUp() {
        userDAO = new MemoryUserDAO();  // Use in-memory implementations
        authDAO = new MemoryAuthDAO();
        gameDAO = new MemoryGameDAO();
        gameService = new GameService(gameDAO, authDAO, userDAO);
    }

    @Test
    public void testClearDatabase() {
        // Arrange: Create a test user and a game
        String username = "testUser";
        String password = "testPassword";
        String email = "testuser@example.com";
        userDAO.createUser(new UserData(username, password, email));


        String gameName = "Chess Game";
        int gameId = gameService.createGame(gameName, "authToken"); // Create a game

        // Assert: Verify user and game exist before clearing
        assertNotNull(userDAO.getUser(username), "User should exist before clearing");
        assertNotNull(gameDAO.findGameById(gameId), "Game should exist before clearing");

        // Act: Clear the database
        gameService.clearDatabase();

        // Assert: Verify user and game no longer exist after clearing
        assertNull(userDAO.getUser(username), "User should be null after clearing");
        assertNull(gameDAO.findGameById(gameId), "Game should be null after clearing");
    }
}
