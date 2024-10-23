package server;

import com.google.gson.Gson;
import dataaccess.BadRequestException;
import dataaccess.DataAccessException;
import dataaccess.UnauthorizedException;
import model.GameData;
import model.GameRequest;
import model.ErrorResponse;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Map;

public class CreateGameHandler implements Route {
    private final GameService gameService;
    private final Gson gson = new Gson();

    public CreateGameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public Object handle(Request req, Response res) {
        try {
            // Get the auth token from the Authorization header
            String authToken = req.headers("authorization");

            if (authToken == null || authToken.isEmpty()) {
                throw new UnauthorizedException("Missing or invalid auth token");
            }

            // Parse the request body to get the game name

            GameRequest game = gson.fromJson(req.body(), GameRequest.class);

            String gameName = game.gameName();

            if (gameName == null || gameName.trim().isEmpty()) {
                res.status(400); // Bad request
                return gson.toJson(new ErrorResponse("Error: bad request - missing or invalid gameName"));
            }

            // Call the service layer to create a new game
            int gameID = gameService.createGame(gameName, authToken);

            // Set response type and status
            res.type("application/json");
            res.status(200);

            // Return the CreateGameResult serialized as JSON
            return gson.toJson(Map.of("gameID", gameID));

        } catch (BadRequestException e) {
            res.status(400);
            return gson.toJson(new ErrorResponse("Error: bad request"));
        } catch (UnauthorizedException e) {
            res.status(401);
            return gson.toJson(new ErrorResponse("Error: unauthorized"));
        } catch (Exception e) {
            res.status(500);
            return gson.toJson(new ErrorResponse("Error: " + e.getMessage()));
        }
    }
}

