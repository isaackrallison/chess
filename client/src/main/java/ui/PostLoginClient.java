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
            String gameName = server.createGame(params[0], authToken);
            return String.format("Game %s created.\n", gameName);
        }
        throw new ResponseException(400, "Expected: create <NAME> ");
        }

    private String list(String... params) throws ResponseException {
        if (params.length == 0) {
            String gameNameIdList = "";
            List<GameData> games = server.listGames(authToken);

            for (GameData game : games) {
                String whitePlayer = game.whiteUsername();
                String blackPlayer = game.blackUsername();
                if (whitePlayer == null) {
                    whitePlayer = "open";
                }
                if (blackPlayer == null) {
                    blackPlayer = "open";
                }
                gameNameIdList += "GameName: " + game.gameName() + ", WhitePlayer: " + whitePlayer + ", BlackPlayer: " + blackPlayer + "\n";

            }

            return String.format("All Games:\n%s", gameNameIdList);
        }
        throw new ResponseException(400, "Expected: list");
    }


    private String join(String... params) throws ResponseException {
        if (params.length == 2) {

            // Check if the game is open
            try {
                server.joinGame(params[1], params[0], authToken);
                ChessBoardUi.main(params);

            } catch (Exception e) {
                throw new ResponseException(400, "It looks like we are having an issue, make sure the color is open, Expected: join <ID> [WHITE|BLACK]");
            }
        }
        return String.format("Joining %s as %s:", params[0], params[1]);
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
                create <NAME>                   - a game
                list                            - games
                join <GameName> [WHITE|BLACK]   - a game
                observe <GameName>              - a game
                logout                          - when you are done
                quit                            - playing chess
                help                            - with possible commands
                """;
    }
}
