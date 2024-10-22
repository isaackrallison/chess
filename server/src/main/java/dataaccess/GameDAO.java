package dataaccess;

import model.GameData;

public interface GameDAO {
    void createGame(GameData game) throws DataAccessException;
    GameData getGame(String gameID) throws DataAccessException;
}


