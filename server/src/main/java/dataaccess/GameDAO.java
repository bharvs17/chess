package dataaccess;

import dataaccess.model.CreateGameReq;
import dataaccess.model.CreateGameRes;
import dataaccess.model.JoinGameReq;
import dataaccess.model.ListGameRes;

public interface GameDAO {

    ListGameRes listGames() throws DataAccessException;

    CreateGameRes makeGame(CreateGameReq gameReq) throws DataAccessException;

    void joinGame(JoinGameReq gameReq, String username) throws DataAccessException;

    void deleteAllGames() throws DataAccessException;

}
