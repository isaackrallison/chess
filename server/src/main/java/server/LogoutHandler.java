package server;

import com.google.gson.Gson;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.UnauthorizedException;
import model.ErrorResponse;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

public class LogoutHandler implements Route {
    private final UserService userService;
    private final Gson gson = new Gson();

    public LogoutHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Object handle(Request req, Response res) {
        try {
            // Parse the JSON request header into an authToken String
            String authToken = req.headers("authorization");

            userService.logout(authToken);

            // Set response type and status
            res.type("application/json");
            res.status(200);

            return "{}";
        } catch (UnauthorizedException e) {
            res.status(401);
            return gson.toJson(new ErrorResponse(e.getMessage()));
        } catch (DataAccessException e) {
            res.status(500);
            return gson.toJson(new ErrorResponse("Error: Unable to access data. Please try again later."));
        }
        catch (Exception e) {
            res.status(500);
            return gson.toJson(new ErrorResponse("Error: " + e));
        }
    }
}
