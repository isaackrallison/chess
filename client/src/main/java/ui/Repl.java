package ui;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl {
    private PreLoginClient preLoginClient;
    private PostLoginClient postLoginClient;
    private final GameplayClient gameplayClient;
    private final String serverUrl;
    private String authToken;

    public Repl(String serverUrl) {
        this.serverUrl = serverUrl;
        preLoginClient = new PreLoginClient(serverUrl);
        gameplayClient = new GameplayClient();
    }

    public void run() {
        System.out.println("\nWelcome to Chess. Register or sign in to get started");
        System.out.print(preLoginClient.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        boolean isLoggedIn = false;

        while (!result.equals("quit") && !isLoggedIn) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = preLoginClient.eval(line);

                if (result.contains("You signed in")) {
                    authToken = extractAuthToken(result);
                    postLoginClient = new PostLoginClient(serverUrl, authToken);
                    System.out.print(postLoginClient.help());
                    isLoggedIn = true;
                } else {
                    System.out.print(SET_TEXT_COLOR_GREEN + result);
                }
            } catch (Throwable e) {
                System.out.print(e.toString());
            }
        }

        while (!result.equals("quit") && isLoggedIn) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = postLoginClient.eval(line);
                System.out.print(SET_TEXT_COLOR_GREEN + result);

                if (result.contains("Logged out")) {
                    run();
                }
            } catch (Throwable e) {
                System.out.print(e.toString());
            }
        }
        System.out.println();
    }

    private String extractAuthToken(String result) {
        String[] lines = result.split("\n");
        return lines.length > 1 ? lines[1] : "";
    }

    private void printPrompt() {
        System.out.print("\n" + RESET_TEXT_COLOR + ">>> " + SET_TEXT_COLOR_GREEN);
    }
}