package dataaccess;

import dataaccess.exceptions.DataAccessException;
import model.UserData;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class MySqlUserDAOTest {
    private MySqlUserDAO userDAO;

    @BeforeEach
    public void setUp() throws DataAccessException {
        // Initialize the MySqlUserDAO before each test
        userDAO = new MySqlUserDAO();
        userDAO.clearUsers(); // Clear existing users before each test
    }

    @Test
    public void testCreateUser() throws DataAccessException {
        UserData user = new UserData("testUser", "testPass", "test@example.com");
        userDAO.createUser(user);

        // Retrieve the user to verify it was created successfully
        UserData retrievedUser = userDAO.getUser("testUser","testPass");
        assertNotNull(retrievedUser);
        assertEquals(user.username(), retrievedUser.username());
        assertEquals(user.email(), retrievedUser.email());
    }

    @Test
    public void testGetUser() throws DataAccessException {
        UserData user = new UserData("testUser", "testPass", "test@example.com");
        userDAO.createUser(user);

        UserData retrievedUser = userDAO.getUser("testUser","testPass");
        assertNotNull(retrievedUser);
        assertEquals("testUser", retrievedUser.username());
    }

    @Test
    public void testGetUserNotFound() throws DataAccessException {
        UserData retrievedUser = userDAO.getUser("nonExistentUser","nonExistentPass");
        assertNull(retrievedUser);
    }

    @Test
    public void testClearUsers() throws DataAccessException {
        UserData user1 = new UserData("user1", "pass1", "user1@example.com");
        UserData user2 = new UserData("user2", "pass2", "user2@example.com");
        userDAO.createUser(user1);
        userDAO.createUser(user2);

        // Clear all users
        userDAO.clearUsers();
        assertNull(userDAO.getUser("user1","pass1"));
        assertNull(userDAO.getUser("user2","pass2"));
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        userDAO.clearUsers(); // Clean up after each test
    }
}
