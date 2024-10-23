package dataaccess;

import model.AuthData;
import model.UserData;

public interface AuthDAO {
    AuthData getAuthToken(String authToken);
    AuthData createAuth(AuthData authData);
    void deleteAuthToken(String authToken);
    void clearAuths();
    boolean validateAuth(String authToken);
}
