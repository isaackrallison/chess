package ui;

import java.util.Scanner;

import static ui.EscapeSequences.*;


public class Repl {
    private final PreLoginClient preLoginClient;
    private final PostLoginClient postLoginClient;
//    private final GameplayClient gameplayClient;


    public Repl(String serverUrl) {
        preLoginClient = new PreLoginClient(serverUrl);
        postLoginClient = new PostLoginClient(serverUrl);
//        gameplayClient = new GameplayClient(serverUrl);
    }

    public void run() {
        System.out.println("\nWelcome to Chess. Register or sign in to get started");
        System.out.print(preLoginClient.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = preLoginClient.eval(line);
                System.out.print(SET_TEXT_COLOR_GREEN + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" + RESET_TEXT_COLOR + ">>> " + SET_TEXT_COLOR_GREEN);
    }

}
