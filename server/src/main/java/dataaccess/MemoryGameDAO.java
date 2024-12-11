package dataaccess;

import model.GameData;
import chess.ChessGame;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MemoryGameDAO implements GameDAO {
    private final Set<GameData> gameStorage = new HashSet<>();

    @Override
    public List<GameData> getAllGames() {
        return new ArrayList<>(gameStorage);
    }

    @Override
    public void createGame(ChessGame game, String gameName) {
        // Create a new GameData instance and add it to the set
        GameData gameData = new GameData(0, null, null, gameName, game);
        gameStorage.add(gameData);
    }

    @Override
    public ChessGame findGameByName(String gameName) {
        for (GameData gameData : gameStorage) {
            if (gameData.gameName() == gameName) {
                return gameData.game();
            }
        }
        return null; // Game not found
    }

    @Override
    public void updateGame(String playerColor, String gameName, String username) {
        GameData updatedGameData = null; // This will hold the updated game data
        for (GameData gameData : gameStorage) {
            if (gameData.gameName() == gameName) {
                if (playerColor.equalsIgnoreCase("BLACK")) {
                    if (gameData.blackUsername() == null) {
                        updatedGameData = new GameData(gameData.gameID(), gameData.whiteUsername(), username, gameData.gameName(), gameData.game());
                    } else {
                        throw new IllegalStateException("Error: color already taken");
                    }
                } else if (playerColor.equalsIgnoreCase("WHITE")) {
                    if (gameData.whiteUsername() == null) {
                        updatedGameData = new GameData(gameData.gameID(), username, gameData.blackUsername(), gameData.gameName(), gameData.game());
                    } else {
                        throw new IllegalStateException("Error: color already taken");
                    }
                }
                break; // Exit the loop after finding the game
            }
        }

        // If we created updatedGameData, remove the old one and add the new one
        if (updatedGameData != null) {
            gameStorage.removeIf(gameData -> gameData.gameName() == gameName); // Remove the old game data
            gameStorage.add(updatedGameData); // Add the updated game data
        }
    }

    @Override
    public void clearGames() {
        gameStorage.clear();
    }
}


