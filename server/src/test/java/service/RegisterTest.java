package service;

import model.AuthData;
import model.UserData;
import service.UserService;
import model.RegisterResult;
import dataaccess.UserDAO;
import dataaccess.AuthDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.MemoryAuthDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterTest {

    private UserDAO userDAO;
    private AuthDAO authDAO;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        userDAO = new MemoryUserDAO();  // Use in-memory implementations
        authDAO = new MemoryAuthDAO();
        userService = new UserService(userDAO, authDAO);
    }

    @Test
    public void testRegisterSuccess() {
        // Arrange
        String username = "newUser";
        String password = "password123";
        String email = "newuser@example.com";

        // Act
        RegisterResult result = userService.register(username, password, email);

        // Assert
        assertNotNull(result);
        assertEquals(username, result.username());
        assertNotNull(result.authToken());

        // Verify user and auth token were created
        UserData createdUser = userDAO.getUser(username);
        assertNotNull(createdUser);
        assertEquals(username, createdUser.username());
        assertEquals(password, createdUser.password());
        assertEquals(email, createdUser.email());

        AuthData authData = authDAO.getAuthToken(result.authToken());
        assertNotNull(authData);
        assertEquals(username, authData.Username());
    }

    @Test
    public void testRegisterUserAlreadyExists() {
        // Arrange
        String username = "existingUser";
        String password = "password123";
        String email = "existinguser@example.com";

        // Create an existing user
        userDAO.createUser(new UserData(username, password, email));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.register(username, password, email);
        });

        assertEquals("User already exists", exception.getMessage());
    }
}

