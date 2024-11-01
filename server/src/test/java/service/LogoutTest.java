package service;

import dataaccess.*;
import model.LoginResult;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LogoutTest {

    private UserDAO userDAO;
    private AuthDAO authDAO;
    private UserService userService;
    private String authToken;

    @BeforeEach
    public void setUp() {
        try {
            userDAO = new MemoryUserDAO();  // Use in-memory implementations
            authDAO = new MemoryAuthDAO();
            userService = new UserService(userDAO, authDAO);

            // Set up a test user for logout tests
            String username = "testUser";
            String password = "testPassword";
            String email = "testuser@example.com";
            userDAO.createUser(new UserData(username, password, email));

            // Simulate login to create an auth token
            LoginResult result = userService.login(username, password);
            authToken = result.authToken();

        } catch (DataAccessException ignored) {

        }
    }
    @Test
    public void testLogoutSuccess() {
        try {
            // Arrange
            String username = "testUser";

            // Act
            userService.logout(authToken);

            // Assert
            assertNull(authDAO.getAuthToken(authToken)); // Verify that the token has been invalidated
        } catch (Exception ignored) {
        }
    }

    @Test
    public void testLogoutInvalidToken() {
        // Arrange
        String invalidAuthToken = "invalidAuthToken";

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.logout(invalidAuthToken);
        });

        assertEquals("Error: unauthorized", exception.getMessage());
    }
}


