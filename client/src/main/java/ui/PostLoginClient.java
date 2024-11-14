package ui;

import java.util.Arrays;

import com.google.gson.Gson;
import exception.ResponseException;
import model.LoginRequest;
import model.RegisterRequest;

public class PostLoginClient {

    private String visitorName = null;
    private final ServerFacade server;
    private final String serverUrl;


    public PostLoginClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }


    public String help() {
        return """
                create <NAME>       - a game
                list                - games
                join <ID> [WHITE|BLACK] - a game
                observe <ID>        - a game
                logout              - when you are done
                quit                - playing chess
                help                - with possible commands
                """;
    }
}
