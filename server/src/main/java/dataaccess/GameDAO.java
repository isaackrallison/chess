package dataaccess;

import chess.ChessGame;
import java.util.List;

public interface GameDAO {
    List<ChessGame> getAllGames();
    void createGame(int GameIdNum, ChessGame game);
    ChessGame findGameById(int gameId);
    void updateGame(String playerColor, int gameId);
    void clearGames();
}




