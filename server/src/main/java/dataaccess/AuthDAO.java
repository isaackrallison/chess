package dataaccess;

import model.AuthData;

public interface AuthDAO {
    AuthData getAuthToken(String authToken) throws DataAccessException;
    AuthData createAuth(AuthData authData) throws DataAccessException;
    void deleteAuthToken(String authToken) throws DataAccessException;
    void clearAuths() throws DataAccessException;
    boolean validateAuth(String authToken) throws DataAccessException;
}

