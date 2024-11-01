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

    public int createGame(String gameName, String authToken) throws DataAccessException {
        if (!authDAO.validateAuth(authToken)) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        ChessGame newGame = new ChessGame();
        gameIdNum++;
        gameDAO.createGame(gameIdNum, newGame, gameName);
        return gameIdNum;
    }

    public List<GameData> listGames(String authToken) throws DataAccessException {
        if (!authDAO.validateAuth(authToken)) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        return gameDAO.getAllGames();
    }

    public void joinGame(int gameId, String playerColor, String authToken) throws DataAccessException {
        // Validate the auth token
        if (!authDAO.validateAuth(authToken)) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        // Find the game by ID
        ChessGame game = gameDAO.findGameById(gameId);
        if (game == null) {
            throw new BadRequestException("Error: bad request");
        }

        // If all checks pass, update the game with the new player
        AuthData data = authDAO.getAuthToken(authToken);
        String username = data.username();
        gameDAO.updateGame(playerColor, gameId, username);
    }


    public void clearDatabase() throws DataAccessException {
        userDAO.clearUsers();
        authDAO.clearAuths();
        gameDAO.clearGames();
    }
}


