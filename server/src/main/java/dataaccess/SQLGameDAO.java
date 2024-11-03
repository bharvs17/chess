package dataaccess;

import dataaccess.model.CreateGameReq;
import dataaccess.model.CreateGameRes;
import dataaccess.model.JoinGameReq;
import dataaccess.model.ListGameRes;


public class SQLGameDAO implements GameDAO {

    @Override
    public ListGameRes listGames() {
        return null;
    }

    @Override
    public CreateGameRes makeGame(CreateGameReq gameReq) throws DataAccessException {
        return null;
    }

    @Override
    public void joinGame(JoinGameReq gameReq, String username) throws DataAccessException {

    }

    @Override
    public void deleteAllGames() {

    }

}
