package dataaccess;

import model.AuthData;

public interface AuthDAO {
    AuthData getAuthToken(String authToken);
    void createAuth(AuthData authData);
    void deleteAuthToken(String authToken);
}
