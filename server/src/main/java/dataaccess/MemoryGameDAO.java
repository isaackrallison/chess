package dataaccess;

import chess.ChessGame;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryGameDAO implements GameDAO {
    private final Map<Integer, ChessGame> gameStorage = new HashMap<>();


    @Override
    public List<ChessGame> getAllGames() {
        return new ArrayList<>(gameStorage.values());
    }

    @Override
    public void createGame(int GameIdNum, ChessGame game) {
        gameStorage.put(GameIdNum, game);
    }

    @Override
    public ChessGame findGameById(int gameId) {
        return gameStorage.get(gameId);
    }

    @Override
    public void updateGame(String playerColor, int gameId) {
        ChessGame game = gameStorage.get(gameId);
        if (game != null) {
            game.setTeamTurn(ChessGame.TeamColor.valueOf(playerColor.toUpperCase()));
        }
    }

    @Override
    public void clearGames() {
        // Clears all games
        gameStorage.clear();
    }
}


