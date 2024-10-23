package service;

import dataaccess.*;
import model.GameData;
import chess.ChessGame;

import java.util.List;

public class GameService {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;
    private final UserDAO userDAO;

    public GameService(GameDAO gameDAO, AuthDAO authDAO, UserDAO userDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
        this.userDAO = userDAO;

    }

    public GameData createGame(String whiteUsername, String blackUsername, String gameName) {
        ChessGame newGame = new ChessGame();
        gameDAO.createGame(newGame);
        return new GameData(1, whiteUsername, blackUsername, gameName, newGame);
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


