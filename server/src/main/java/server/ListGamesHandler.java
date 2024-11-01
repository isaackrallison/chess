package server;

import com.google.gson.Gson;
import dataaccess.exceptions.DataAccessException;
import model.GameData;
import dataaccess.exceptions.UnauthorizedException;
import service.GameService;
import model.ErrorResponse;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.List;
import java.util.Map;

public class ListGamesHandler implements Route {
    private final GameService gameService;
    private final Gson gson = new Gson();

    public ListGamesHandler(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public Object handle(Request req, Response res) {
        try {
            // Get the auth token from the Authorization header
            String authToken = req.headers("authorization");

            // Validate the authorization token
            if (authToken == null || authToken.isEmpty()) {
                throw new UnauthorizedException("Error: unauthorized");
            }

            List<GameData> games = gameService.listGames(authToken);

            // Set response type and status
            res.type("application/json");
            res.status(200);

            // Return the games serialized as JSON
            return gson.toJson(Map.of("games", games));

        } catch (UnauthorizedException e) {
            res.status(401);
            return gson.toJson(new ErrorResponse(e.getMessage()));
        } catch (DataAccessException e) {
            res.status(500);
            return gson.toJson(new ErrorResponse("Error: Unable to access data. Please try again later."));
        }catch (Exception e) {
            res.status(500);
            return gson.toJson(new ErrorResponse("Error: " + e.getMessage()));
        }
    }
}

