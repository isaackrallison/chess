package server;

import com.google.gson.Gson;
import model.RegisterRequest;
import model.RegisterResult;
import service.UserService;
import model.ErrorResponse;
import spark.Request;
import spark.Response;
import spark.Route;
import dataaccess.ExistsException;

public class RegisterHandler implements Route {
    private final UserService userService;
    private final Gson gson = new Gson();

    public RegisterHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Object handle(Request req, Response res) {
        try {
            // Parse the JSON request body into a RegisterRequest object
            RegisterRequest registerRequest = gson.fromJson(req.body(), RegisterRequest.class);

            // Call the service layer to register the user
            RegisterResult result = userService.register(
                    registerRequest.username(),
                    registerRequest.password(),
                    registerRequest.email()
            );

            // Set response type and status for success
            res.type("application/json");
            res.status(200);

            // Return the RegisterResult serialized as JSON
            return gson.toJson(result);
        } catch (ExistsException e) {
            // Set response code for username already taken
            res.status(403);
            return gson.toJson(new ErrorResponse("Error: already taken"));
        } catch (IllegalArgumentException e) {
            // Set response code for bad request (invalid input)
            res.status(400);
            return gson.toJson(new ErrorResponse("Error: bad request"));
        } catch (Exception e) {
            // Handle any other unexpected errors
            res.status(500);
            return gson.toJson(new ErrorResponse("Error: unexpected server error"));
        }
    }
}





