package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;

public class AuthService {
    private final AuthDAO authDAO;
    private final UserDAO userDAO;

    public AuthService(AuthDAO authDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }

    public AuthData verifyAuth(String authToken) throws DataAccessException {
        AuthData authData = authDAO.getAuthToken(authToken);
        if (authData == null) {
            throw new RuntimeException("Invalid auth token");
        }
        return authData; // Return the AuthData if the token is valid
    }

    public void logout(String authToken) throws DataAccessException {
        AuthData authData = authDAO.getAuthToken(authToken);
        if (authData == null) {
            throw new RuntimeException("Invalid auth token");
        }
        authDAO.deleteAuthToken(authToken); // Remove the auth token
    }
}


