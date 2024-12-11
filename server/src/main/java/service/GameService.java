package service;

import dataaccess.*;
import dataaccess.exceptions.BadRequestException;
import dataaccess.exceptions.DataAccessException;
import model.AuthData;
import model.GameData;
import dataaccess.exceptions.UnauthorizedException;
import chess.ChessGame;

import java.util.List;

public class GameService {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;
    private final UserDAO userDAO;
    private int gameIdNum;

    public GameService(GameDAO gameDAO, AuthDAO authDAO, UserDAO userDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
        this.userDAO = userDAO;
        this.gameIdNum = 0;


    }

    public String createGame(String gameName, String authToken) throws DataAccessException {
        if (!authDAO.validateAuth(authToken)) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        ChessGame newGame = new ChessGame();
        gameDAO.createGame(newGame, gameName);
        return gameName;
    }

    public List<GameData> listGames(String authToken) throws DataAccessException {
        if (!authDAO.validateAuth(authToken)) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        return gameDAO.getAllGames();
    }

    public void joinGame(String gameName, String playerColor, String authToken) throws DataAccessException {
        // Validate the auth token
        if (!authDAO.validateAuth(authToken)) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        // Find the game by ID
//        ChessGame game = gameDAO.findGameById(gameId);
        ChessGame game = gameDAO.findGameByName(gameName);
        if (game == null) {
            throw new BadRequestException("Error: bad request");
        }

        // If all checks pass, update the game with the new player
        AuthData data = authDAO.getAuthToken(authToken);
        String username = data.username();
        gameDAO.updateGame(playerColor, gameName, username);
    }


    public void clearDatabase() throws DataAccessException {
        userDAO.clearUsers();
        authDAO.clearAuths();
        gameDAO.clearGames();
    }
}


