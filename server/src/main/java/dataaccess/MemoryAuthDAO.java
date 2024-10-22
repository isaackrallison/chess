package dataaccess;

import model.AuthData;
import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO {
    private final Map<String, AuthData> authStorage = new HashMap<>();

    @Override
    public AuthData getAuthToken(String authToken) {
        // Retrieve AuthData by auth token
        return authStorage.get(authToken);
    }

    @Override
    public void createAuth(AuthData authData) {
        // Store AuthData by token
        authStorage.put(authData.authToken(), authData);
    }

    @Override
    public void deleteAuthToken(String authToken) {
        // Remove AuthData by auth token
        authStorage.remove(authToken);
    }
}


