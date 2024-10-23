package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.model.CreateGameReq;
import dataaccess.model.CreateGameRes;
import dataaccess.model.JoinGameReq;
import dataaccess.model.ListGameRes;
import model.GameData;

import java.util.Collection;

public class GameService {

    private final GameDAO gameAccess;

    public GameService(GameDAO gameAccess) {
        this.gameAccess = gameAccess;
    }

    public ListGameRes listGames() {
        return gameAccess.listGames();
    }

    public CreateGameRes makeGame(CreateGameReq gameReq) throws DataAccessException {
        return gameAccess.makeGame(gameReq);
    }

    public void joinGame(JoinGameReq gameReq, String username) throws DataAccessException {
        gameAccess.joinGame(gameReq, username);
    }

    public void deleteAllGames() {
        gameAccess.deleteAllGames();
    }

    //OLD BELOW

    public int createGame(String gameName) throws DataAccessException {
        return gameAccess.createGame(gameName);
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


}
