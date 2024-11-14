package ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import exception.ResponseException;
import model.GameData;
import ui.GameplayClient;
import ui.*;

public class PostLoginClient {
    private final ServerFacade server;
    private final String serverUrl;
    private final String authToken;


    public PostLoginClient(String serverUrl, String authToken) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.authToken = authToken;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "create" -> create(params);
                case "list" -> list(params);
                case "join" -> join(params);
                case "observe" -> observe(params);
                case "logout" -> logout(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    private String create(String... params) throws ResponseException{
        if (params.length == 1) {
            int GameIdNum = server.createGame(params[0], authToken);
            return String.format("Game %s created.\n", GameIdNum);
        }
        throw new ResponseException(400, "Expected: create <NAME> ");
        }

    private String list(String... params) throws ResponseException {
        if (params.length == 0) {
            String gameNameIdList = "";
            List<GameData> games = server.listGames(authToken);

            for (GameData game : games) {
                gameNameIdList += "ID: " + game.gameID() + ", Name: " + game.gameName() + "\n";
            }

            return String.format("All Games:\n%s", gameNameIdList);
        }
        throw new ResponseException(400, "Expected: list");
    }


    private String join(String... params) throws ResponseException{
        if (params.length == 2) {
            server.joinGame(params[1], Integer.parseInt(params[0]));
            ChessBoardUi.main(params);
            return String.format("Joining %s as %s:", params[0], params[1]);
        }
        throw new ResponseException(400, "Expected: join <ID> [WHITE|BLACK]");
    }

    private String observe(String... params) throws ResponseException {
        if (params.length == 1) {
            ChessBoardUi.main(params);
            return String.format("Observing game %s:", params[0]);
        }
        throw new ResponseException(400, "Expected: observe <ID> ");
    }

    private String logout(String... params) throws ResponseException {
        if (params.length == 0) {
            server.logout(authToken);
            return String.format("Logged out");
        }
        throw new ResponseException(400, "Expected: logout");
    }

    public String help() {
        return """
                create <NAME>           - a game
                list                    - games
                join <ID> [WHITE|BLACK] - a game
                observe <ID>            - a game
                logout                  - when you are done
                quit                    - playing chess
                help                    - with possible commands
                """;
    }
}
