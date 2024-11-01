package dataaccess;

import model.UserData;

public interface UserDAO {
    UserData getUser(String username, String password) throws DataAccessException;
    void createUser(UserData userData) throws DataAccessException;
    void clearUsers() throws DataAccessException;
}



