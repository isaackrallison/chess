package server;

import com.google.gson.Gson;
import model.LoginRequest;
import model.LoginResult;
import model.ErrorResponse;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

public class LoginHandler implements Route {
    private final UserService userService;
    private final Gson gson = new Gson();

    public LoginHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Object handle(Request req, Response res) {
        try {
            // Parse the JSON request body into a LoginRequest object
            LoginRequest loginRequest = gson.fromJson(req.body(), LoginRequest.class);

            // Call the service layer to authenticate the user
            LoginResult result = userService.login(
                    loginRequest.username(),
                    loginRequest.password()
            );

            // Set response type and status
            res.type("application/json");
            res.status(200);

            // Return the LoginResult serialized as JSON
            return gson.toJson(result);
        } catch (Exception e) {
            // Handle errors and return a meaningful response
            res.status(400); // Bad request
            return gson.toJson(new ErrorResponse(e.getMessage()));
        }
    }
}
