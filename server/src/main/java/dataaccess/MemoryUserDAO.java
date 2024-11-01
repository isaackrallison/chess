package dataaccess;

import model.UserData;
import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDAO {
    private final Map<String, UserData> userStorage = new HashMap<>();

    @Override
    public UserData getUser(String username,String password) {
        return userStorage.get(username); // Retrieve the user by username
    }

    @Override
    public void createUser(UserData userData) {
        userStorage.put(userData.username(), userData); // Store user data by username
    }

    @Override
    public void clearUsers () {
        userStorage.clear();
    }
}



