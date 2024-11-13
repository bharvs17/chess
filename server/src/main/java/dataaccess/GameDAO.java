package dataaccess;

import model.CreateGameReq;
import model.CreateGameRes;
import model.JoinGameReq;
import model.ListGameRes;

public interface GameDAO {

    ListGameRes listGames() throws DataAccessException;

    CreateGameRes makeGame(CreateGameReq gameReq) throws DataAccessException;

    void joinGame(JoinGameReq gameReq, String username) throws DataAccessException;

    void deleteAllGames() throws DataAccessException;

}
