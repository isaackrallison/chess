package dataaccess;

import chess.ChessGame;
import dataaccess.exceptions.DataAccessException;
import model.GameData;

import java.util.List;

public interface GameDAO {
    List<GameData> getAllGames() throws DataAccessException;
    void createGame(ChessGame game, String gameName) throws DataAccessException;
    ChessGame findGameByName(String gameName) throws DataAccessException;
    void updateGame(String playerColor, String gameName, String username) throws DataAccessException;
    void clearGames() throws DataAccessException;
}




