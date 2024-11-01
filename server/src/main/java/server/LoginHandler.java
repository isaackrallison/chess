package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.UnauthorizedException;
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
        } catch (UnauthorizedException e) {
            res.status(401);
            return gson.toJson(new ErrorResponse(e.getMessage()));
        } catch (DataAccessException e) {
            res.status(500);
            return gson.toJson(new ErrorResponse("Error: Unable to access data. Please try again later."));
        } catch (Exception e) {
            res.status(500);
            return gson.toJson(new ErrorResponse("Error: " + e));
        }
    }
}
