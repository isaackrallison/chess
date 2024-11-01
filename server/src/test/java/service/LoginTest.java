//package service;
//
//import model.AuthData;
//import model.LoginResult;
//import model.UserData;
//import service.UserService;
//import dataaccess.UserDAO;
//import dataaccess.AuthDAO;
//import dataaccess.MemoryUserDAO;
//import dataaccess.MemoryAuthDAO;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class LoginTest {
//
//    private UserDAO userDAO;
//    private AuthDAO authDAO;
//    private UserService userService;
//
//    @BeforeEach
//    public void setUp() {
//        userDAO = new MemoryUserDAO();  // Use in-memory implementations
//        authDAO = new MemoryAuthDAO();
//        userService = new UserService(userDAO, authDAO);
//
//        // Set up a test user for login tests
//        String username = "testUser";
//        String password = "testPassword";
//        String email = "testuser@example.com";
//        userDAO.createUser(new UserData(username, password, email));
//    }
//
//    @Test
//    public void testLoginSuccess() {
//        // Arrange
//        String username = "testUser";
//        String password = "testPassword";
//
//        // Act
//        LoginResult result = userService.login(username, password);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(username, result.username());
//        assertNotNull(result.authToken());
//    }
//
//    @Test
//    public void testLoginInvalidUsername() {
//        // Arrange
//        String username = "invalidUser";
//        String password = "testPassword";
//
//        // Act & Assert
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
//            userService.login(username, password);
//        });
//
//        assertEquals("Error: unauthorized", exception.getMessage());
//    }
//
//    @Test
//    public void testLoginInvalidPassword() {
//        // Arrange
//        String username = "testUser";
//        String password = "wrongPassword";
//
//        // Act & Assert
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
//            userService.login(username, password);
//        });
//
//        assertEquals("Error: unauthorized", exception.getMessage());
//    }
//}

package service;

import dataaccess.*;
import model.AuthData;
import model.LoginResult;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {

    private UserDAO userDAO;
    private AuthDAO authDAO;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        try {
            userDAO = new MemoryUserDAO();  // Use in-memory implementations
            authDAO = new MemoryAuthDAO();
            userService = new UserService(userDAO, authDAO);

            // Set up a test user for login tests
            String username = "testUser";
            String password = "testPassword";
            String email = "testuser@example.com";
            userDAO.createUser(new UserData(username, password, email));
        } catch (DataAccessException ignored) {
        }
    }

    @Test
    public void testLoginSuccess() {
        try {
            // Arrange
            String username = "testUser";
            String password = "testPassword";

            // Act
            LoginResult result = userService.login(username, password);

            // Assert
            assertNotNull(result);
            assertEquals(username, result.username());
            assertNotNull(result.authToken());
        } catch (DataAccessException ignored) {
        }
    }

    @Test
    public void testLoginInvalidUsername() {
        // Arrange
        String username = "invalidUser";
        String password = "testPassword";

        // Act & Assert
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
            userService.login(username, password);
        });

        assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    public void testLoginInvalidPassword() {
        // Arrange
        String username = "testUser";
        String password = "wrongPassword";

        // Act & Assert
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
            userService.login(username, password);
        });

        assertEquals("Error: unauthorized", exception.getMessage());
    }
}


