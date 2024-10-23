package service;

import dataaccess.*;
import model.AuthData;
import model.RegisterResult;
import model.UserData;
import model.LoginResult;

import static service.AuthTokenCreator.generateToken;

public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public RegisterResult register(String username, String password, String email) {
        UserData existingUser = userDAO.getUser(username);
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

    public LoginResult login(String username, String password) {
        UserData user = userDAO.getUser(username);
        if (user == null || !user.password().equals(password)) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        AuthData authData = authDAO.createAuth(new AuthData("Token", username));
        return new LoginResult(user.username(), authData.authToken());
    }

    public void logout(String authToken) {
        AuthData authData = authDAO.getAuthToken(authToken);
        if (authData == null) {
            throw new RuntimeException("Invalid authentication token");
        }
        authDAO.deleteAuthToken(authToken);
    }
}


