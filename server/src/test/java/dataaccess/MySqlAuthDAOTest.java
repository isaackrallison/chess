package dataaccess;

import dataaccess.DataAccessException;
import dataaccess.MySqlAuthDAO;
import model.AuthData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MySqlAuthDAOTest {
    private MySqlAuthDAO authDAO;

    @BeforeEach
    public void setUp() throws DataAccessException {
        authDAO = new MySqlAuthDAO();
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        authDAO.clearAuths(); // Clean up after tests
    }

    @Test
    public void testCreateAuthPositive() throws DataAccessException {
        AuthData authData = new AuthData("token123", "username");
        AuthData createdAuth = authDAO.createAuth(authData);
        assertNotNull(createdAuth);
        assertEquals("token123", createdAuth.authToken());
        assertEquals("username", createdAuth.username());
    }

    @Test
    public void testCreateAuthNegative() throws DataAccessException {
        AuthData authData = new AuthData("token123", "username");
        authDAO.createAuth(authData);

        assertThrows(DataAccessException.class, () -> authDAO.createAuth(authData));
    }

    @Test
    public void testGetAuthTokenPositive() throws DataAccessException {
        AuthData authData = new AuthData("token123", "username");
        authDAO.createAuth(authData);
        AuthData retrievedAuth = authDAO.getAuthToken("token123");
        assertNotNull(retrievedAuth);
        assertEquals("username", retrievedAuth.username());
    }

    @Test
    public void testGetAuthTokenNegative() throws DataAccessException {
        assertNull(authDAO.getAuthToken("invalidToken"));
    }

    @Test
    public void testDeleteAuthTokenPositive() throws DataAccessException {
        AuthData authData = new AuthData("token123", "username");
        authDAO.createAuth(authData);
        authDAO.deleteAuthToken("token123");
        assertNull(authDAO.getAuthToken("token123"));
    }

    @Test
    public void testDeleteAuthTokenNegative() throws DataAccessException {
        authDAO.deleteAuthToken("invalidToken"); // Should not throw an error
        assertNull(authDAO.getAuthToken("invalidToken"));
    }

    @Test
    public void testValidateAuthPositive() throws DataAccessException {
        AuthData authData = new AuthData("token123", "username");
        authDAO.createAuth(authData);
        assertTrue(authDAO.validateAuth("token123"));
    }

    @Test
    public void testValidateAuthNegative() throws DataAccessException {
        assertFalse(authDAO.validateAuth("invalidToken"));
    }

    @Test
    public void testClearAuths() throws DataAccessException {
        AuthData authData = new AuthData("token123", "username");
        authDAO.createAuth(authData);
        authDAO.clearAuths();
        assertNull(authDAO.getAuthToken("token123"));
    }
}

