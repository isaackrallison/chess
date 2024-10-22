import chess.*;
import server.Server;
import service.UserService;
import dataaccess.MemoryUserDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);

        // Create DAO instances
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();

        // Pass DAO instances to UserService
        UserService userService = new UserService(userDAO, authDAO);

        // Pass UserService to Server
        Server server = new Server(userService);

        // Start server
        int port = 8080;
        server.run(port);
    }
}

