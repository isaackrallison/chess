package server;

import dataaccess.*;
import dataaccess.exceptions.DataAccessException;
import service.AuthService;
import service.GameService;
import service.UserService;
import spark.Spark;

public class Server {

    public void setUpHandlers(UserService userService, GameService gameService) {
        // Define routes and pass the relevant service to the handlers
        Spark.post("/user", new RegisterHandler(userService));
        Spark.post("/session", new LoginHandler(userService));
        Spark.delete("/db", new ClearHandler(gameService));
        Spark.delete("/session", new LogoutHandler(userService));
        Spark.post("/game", new CreateGameHandler(gameService));
        Spark.get("/game", new ListGamesHandler(gameService));
        Spark.put("/game", new JoinGameHandler(gameService));
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        try {
            // Create DAO instances
            MySqlUserDAO userDAO = new MySqlUserDAO();
            MySqlGameDAO gameDAO = new MySqlGameDAO();
            MySqlAuthDAO authDAO = new MySqlAuthDAO();




            // Pass DAO instances to UserService
            UserService userService = new UserService(userDAO, authDAO);
            GameService gameService = new GameService(gameDAO, authDAO, userDAO);
            AuthService authService = new AuthService(authDAO, userDAO);

        setUpHandlers(userService, gameService);

        }catch (DataAccessException ignored) {}

        // Initialize the server
        Spark.init();
        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}




