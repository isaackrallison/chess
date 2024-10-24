package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.List;

public interface GameDAO {
    List<GameData> getAllGames();
    void createGame(int GameIdNum, ChessGame game, String gameName);
    ChessGame findGameById(int gameId);
    void updateGame(String playerColor, int gameId, String username);
    void clearGames();
}




