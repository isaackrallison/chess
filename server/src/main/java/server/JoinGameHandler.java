package server;

import com.google.gson.Gson;
import dataaccess.exceptions.BadRequestException;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.UnauthorizedException;
import model.JoinGameRequest;
import model.ErrorResponse;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class JoinGameHandler implements Route {
    private final GameService gameService;
    private final Gson gson = new Gson();

    public JoinGameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public Object handle(Request req, Response res) {
        try {
            // Get the auth token from the Authorization header
            String authToken = req.headers("authorization");

            // Check for a missing or invalid auth token
            if (authToken == null || authToken.isEmpty()) {
                throw new UnauthorizedException("Missing or invalid auth token");
            }

            // Parse the request body to get the game ID and player color
            JoinGameRequest gameRequest = gson.fromJson(req.body(), JoinGameRequest.class);

            // Validate the game ID and player color
            String gameName = gameRequest.gameName();
            String playerColor = gameRequest.playerColor(); // Assuming GameRequest has a method playerColor()

//            if (gameId <= 0 || playerColor == null || playerColor.isEmpty()) {
//                res.status(400); // Bad request
//                return gson.toJson(new ErrorResponse("Error: bad request - missing or invalid gameId or playerColor"));
//            }


            // Call the service layer to join the game
            gameService.joinGame(gameName, playerColor, authToken);

            // Set response type and status
            res.type("application/json");
            res.status(200);
            return "{}"; // Return an empty JSON object on success

        } catch (BadRequestException e) {
            res.status(400);
            return gson.toJson(new ErrorResponse("Error: bad request"));
        } catch (UnauthorizedException e) {
            res.status(401);
            return gson.toJson(new ErrorResponse("Error: unauthorized"));
        } catch (IllegalStateException e) { // Custom exception for already taken
            res.status(403);
            return gson.toJson(new ErrorResponse("Error: already taken"));
        } catch (DataAccessException e) {
            res.status(500);
            return gson.toJson(new ErrorResponse("Error: Unable to access data. Please try again later."));
        }catch (Exception e) {
            res.status(500);
            return gson.toJson(new ErrorResponse("Error: " + e.getMessage()));
        }
    }
}

