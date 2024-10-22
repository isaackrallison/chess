package server;

import com.google.gson.Gson;
import model.RegisterRequest;
import model.RegisterResult;
import model.ErrorResponse;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

public class RegisterHandler implements Route {
    private final UserService userService;
    private final Gson gson = new Gson();

    // Constructor to pass UserService
    public RegisterHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Object handle(Request req, Response res) {
        try {
            // Parse the incoming JSON request body into RegisterRequest object
            RegisterRequest registerRequest = gson.fromJson(req.body(), RegisterRequest.class);

            // Call the service layer to process registration
            RegisterResult result = userService.register(
                    registerRequest.username(),
                    registerRequest.password(),
                    registerRequest.email()
            );

            // Set response type to JSON and status to 200 (OK)
            res.type("application/json");
            res.status(200);

            // Return the result as a JSON response
            return gson.toJson(result);

        } catch (Exception e) {
            // In case of errors, set the status to 400 (Bad Request)
            res.status(400);

            // Return a JSON error response
            return gson.toJson(new ErrorResponse(e.getMessage()));
        }
    }
}




