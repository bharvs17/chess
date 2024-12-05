package dataaccess;

import chess.ChessGame;
import model.CreateGameReq;
import model.CreateGameRes;
import model.JoinGameReq;
import model.ListGameRes;
import exception.DataAccessException;

public interface GameDAO {

    ListGameRes listGames() throws DataAccessException;

    CreateGameRes makeGame(CreateGameReq gameReq) throws DataAccessException;

    void joinGame(JoinGameReq gameReq, String username) throws DataAccessException;

    void deleteAllGames() throws DataAccessException;

    ChessGame getGame(int gameID) throws DataAccessException;

}
