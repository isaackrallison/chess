package service;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import model.LoginReturn;

public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public UserData register(String username, String password, String email) {
        UserData existingUser = userDAO.getUser(username);
        if (existingUser != null) {
            throw new RuntimeException("User already exists");
        }
        UserData newUser = new UserData(username, password, email);
        userDAO.createUser(newUser);
        AuthData authData = authDAO.createAuth(new AuthData("Token", newUser.username()));
        return new UserData(newUser.username(), newUser.password(), authData.authToken());
    }

    public LoginReturn login(String username, String password) {
        UserData user = userDAO.getUser(username);
        if (user == null || !user.password().equals(password)) {
            throw new RuntimeException("Invalid username or password");
        }
        AuthData authData = authDAO.createAuth(new AuthData("Token", username));
        return new LoginReturn(user.username(), authData.authToken());
    }

    public void logout(String authToken) {
        AuthData authData = authDAO.getAuthToken(authToken);
        if (authData == null) {
            throw new RuntimeException("Invalid authentication token");
        }
        authDAO.deleteAuthToken(authToken);
    }
}


