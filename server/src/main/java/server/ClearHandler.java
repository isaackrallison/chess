package server;

import model.ErrorResponse;
import spark.Request;
import spark.Response;
import spark.Route;
import service.UserService;
import service.GameService;
import service.AuthService;
import com.google.gson.Gson;

import java.util.HashSet;
import java.util.Set;

public class ClearHandler implements Route {
    private final GameService gameService;
    private final Gson gson = new Gson();

    public ClearHandler(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public Object handle(Request req, Response res) {
        try {
            // Clear the users, games, and auth tokens
            gameService.clearDatabase();

            // Set response type and status
            res.type("application/json");
            res.status(200); // Success

            // Return an empty JSON object
            return "{}";
        } catch (Exception e) {
            // Handle any other unexpected errors
            res.status(500);
            return gson.toJson(new ErrorResponse("Error: " + e));
        }
    }
}

