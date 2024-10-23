package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.MemoryGameDAO;
import model.GameData;

import java.util.Collection;

public class GameService {

    private final GameDAO gameAccess;

    public GameService(GameDAO gameAccess) {
        this.gameAccess = gameAccess;
    }

    public int createGame(String gameName, int gameID) throws DataAccessException {
        return gameAccess.createGame(gameName, gameID);
    }

    public Collection<GameData> getAllGames() {
        return gameAccess.getAllGames();
    }

    public GameData getGame(int gameID) throws DataAccessException {
        return gameAccess.getGame(gameID);
    }

    public void joinGame(int gameID, ChessGame.TeamColor color, String username) throws DataAccessException {
        gameAccess.joinGame(gameID, color, username);
    }

    public void deleteAll() {
        gameAccess.deleteAll();
    }

}
