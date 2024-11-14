package ui;

import java.util.Arrays;

import exception.ResponseException;
import model.LoginRequest;
import model.RegisterRequest;

public class PreLoginClient {
    private final ServerFacade server;
    private final String serverUrl;

    public PreLoginClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }


    public String register(String... params) throws ResponseException {
        if (params.length == 3) {
            var data = server.register(new RegisterRequest(params[0], params[1], params[2]));
            String autToken = data.authToken();
            return String.format("You signed in as %s.\n" + autToken, params[0]);
        }
        throw new ResponseException(400, "Expected: register <USERNAME> <PASSWORD> <EMAIL>");
        }

    public  String login(String... params) throws ResponseException {
        if (params.length == 2) {
            var data = server.login(new LoginRequest(params[0], params[1]));
            String authToken = data.authToken();
            return String.format("You signed in as %s.\n" + authToken, params[0]);
        }
        throw new ResponseException(400, "Expected: login <USERNAME> <PASSWORD>");
    }

    public String help() {
            return """
                    register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                    login <USERNAME> <PASSWORD>            - to play chess
                    quit                                   - playing chess
                    help                                   - with possible commands
                    """;
    }
}
