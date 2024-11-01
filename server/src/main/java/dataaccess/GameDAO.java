package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.List;

public interface GameDAO {
    List<GameData> getAllGames() throws DataAccessException;
    void createGame(int GameIdNum, ChessGame game, String gameName) throws DataAccessException;
    ChessGame findGameById(int gameId) throws DataAccessException;
    void updateGame(String playerColor, int gameId, String username) throws DataAccessException;
    void clearGames() throws DataAccessException;
}




