package service;

import dataaccess.*;
import model.GameData;
import dataaccess.UnauthorizedException;
import chess.ChessGame;

import java.util.List;

public class GameService {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;
    private final UserDAO userDAO;
    private int GameIdNum;

    public GameService(GameDAO gameDAO, AuthDAO authDAO, UserDAO userDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
        this.userDAO = userDAO;
        this.GameIdNum = 0;


    }

    public int createGame(String gameName, String authToken) {
        if (!authDAO.validateAuth(authToken)) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        ChessGame newGame = new ChessGame();
        GameIdNum++;
        gameDAO.createGame(GameIdNum, newGame);
        return GameIdNum;
    }

    public List<ChessGame> listGames() {
        return gameDAO.getAllGames();
    }

    public void joinGame(int gameId, String playerColor) {
        ChessGame game = gameDAO.findGameById(gameId);
        if (game == null) {
            throw new RuntimeException("Game not found");
        }
        gameDAO.updateGame(playerColor, gameId);
    }

    public void clearDatabase() {
        userDAO.clearUsers();
        authDAO.clearAuths();
        gameDAO.clearGames();
    }
}


