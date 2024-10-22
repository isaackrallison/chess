package server;

import com.google.gson.Gson;
import service.UserService;
import spark.Spark;
import dataaccess.MemoryUserDAO;
import dataaccess.MemoryAuthDAO;
import model.RegisterRequest;
import model.RegisterResult;

public class Server {

    private final UserService userService;
    private final Gson gson = new Gson();

    public Server() {
        // Initialize services with memory-based DAOs for now
        this.userService = new UserService(new MemoryUserDAO(), new MemoryAuthDAO());
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);
        Spark.staticFiles.location("web");

        registerEndpoints();

        Spark.init();
        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private void registerEndpoints() {
        Spark.post("/user", (req, res) -> {
            RegisterRequest request = gson.fromJson(req.body(), RegisterRequest.class);
            try {
                RegisterResult result = new RegisterResult(request.getUsername(), "auth");
                res.status(200);
                return gson.toJson(result);
            } catch (RuntimeException e) {
                res.status(400);
                return gson.toJson(new ErrorResponse(e.getMessage()));
            }
        });

    }

    // A simple class to wrap error messages in JSON
    private static class ErrorResponse {
        private final String error;

        public ErrorResponse(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }
    }
}

