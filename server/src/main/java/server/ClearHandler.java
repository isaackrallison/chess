package server;

import dataaccess.exceptions.DataAccessException;
import model.ErrorResponse;
import spark.Request;
import spark.Response;
import spark.Route;
import service.GameService;
import com.google.gson.Gson;


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
        } catch (DataAccessException e) {
            res.status(500);
            return gson.toJson(new ErrorResponse("Error: Unable to access data. Please try again later."));
        }catch (Exception e) {
            // Handle any other unexpected errors
            res.status(500);
            return gson.toJson(new ErrorResponse("Error: " + e));
        }
    }
}

