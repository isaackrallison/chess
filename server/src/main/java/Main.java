import chess.*;
import server.Server;

public class Main {
    public static void main(String[] args) {
        System.out.println("♕ 240 Chess Server: ");

        // Pass UserService to Server
        Server server = new Server();


        // Start server
        int port = 8080;
        server.run(port);
    }
}


