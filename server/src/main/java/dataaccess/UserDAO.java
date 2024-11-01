package dataaccess;

import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.UnauthorizedException;
import model.UserData;

public interface UserDAO {
    UserData getUser(String username, String password) throws DataAccessException;
    void createUser(UserData userData) throws DataAccessException;
    void clearUsers() throws DataAccessException;
    boolean verifyUser(String username, String providedClearTextPassword) throws UnauthorizedException;
}



