package service;

import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.model.CreateGameReq;
import dataaccess.model.CreateGameRes;
import dataaccess.model.JoinGameReq;
import dataaccess.model.ListGameRes;

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

}
