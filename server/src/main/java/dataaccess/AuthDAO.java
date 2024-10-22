package dataaccess;

import model.AuthData;
import model.UserData;

public interface AuthDAO {
    AuthData getAuthToken(String authToken);
    AuthData createAuth(UserData authData);
    void deleteAuthToken(String authToken);
}
