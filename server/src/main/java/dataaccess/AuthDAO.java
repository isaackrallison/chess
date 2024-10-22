package dataaccess;

import model.AuthData;
import model.UserData;

public interface AuthDAO {
    AuthData register(UserData user);
    AuthData login(UserData user);
    void logout(AuthData auth);
}