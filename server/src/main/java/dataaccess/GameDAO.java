package dataaccess;

import chess.ChessGame;
import dataaccess.exceptions.DataAccessException;
import model.GameData;

import java.util.List;

public interface GameDAO {
    List<GameData> getAllGames() throws DataAccessException;
    void createGame(int gameIdNum, ChessGame game, String gameName) throws DataAccessException;
    ChessGame findGameById(int gameId) throws DataAccessException;
    void updateGame(String playerColor, int gameId, String username) throws DataAccessException;
    void clearGames() throws DataAccessException;
}




