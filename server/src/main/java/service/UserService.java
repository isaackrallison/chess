package service;

import dataaccess.*;
import model.AuthData;
import model.RegisterResult;
import model.UserData;
import model.LoginResult;
import dataaccess.DataAccessException;
import dataaccess.MySqlUserDAO;

import static service.AuthTokenCreator.generateToken;

public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public RegisterResult register(String username, String password, String email) throws DataAccessException {
        UserData existingUser = userDAO.getUser(username, password);
        if (username == null|| password == null|| email == null) {
            throw new IllegalArgumentException("Error: bad request");
        } else if (existingUser != null) {
            throw new ExistsException("User already exists");
        }
        UserData newUser = new UserData(username, password, email);
        userDAO.createUser(newUser);

        AuthData authData = authDAO.createAuth(new AuthData(generateToken(), newUser.username()));
        return new RegisterResult(newUser.username(), authData.authToken());
    }

    public LoginResult login(String username, String password) throws DataAccessException {
        if (!userDAO.verifyUser(username,password)) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        UserData user = userDAO.getUser(username, password);

        AuthData authData = authDAO.createAuth(new AuthData(generateToken(), username));
        return new LoginResult(user.username(), authData.authToken());
    }

    public void logout(String authToken) throws DataAccessException {
            if (!authDAO.validateAuth(authToken)) {
                throw new UnauthorizedException("Error: unauthorized");
            }
            AuthData authData = authDAO.getAuthToken(authToken);
            authDAO.deleteAuthToken(authToken);
    }
}


