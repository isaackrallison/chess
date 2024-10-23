package server;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import service.GameService;
import service.UserService;
import spark.Spark;

public class Server {

    public void setUpHandlers(UserService userService, GameService gameService) {
        // Define routes and pass the relevant service to the handlers
        Spark.post("/user", new RegisterHandler(userService));
        Spark.post("/session", new LoginHandler(userService));
        Spark.delete("/db", new ClearHandler(gameService));
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Create DAO instances
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();

        // Pass DAO instances to UserService
        UserService userService = new UserService(userDAO, authDAO);
        GameService gameService = new GameService(gameDAO, authDAO, userDAO);

        setUpHandlers(userService, gameService);

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




