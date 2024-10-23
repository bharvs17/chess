package dataaccess;

import chess.ChessGame;
import dataaccess.model.CreateGameReq;
import dataaccess.model.CreateGameRes;
import dataaccess.model.JoinGameReq;
import dataaccess.model.ListGameRes;
import model.GameData;

import java.util.Collection;

public interface GameDAO {

    public ListGameRes listGames();

    public CreateGameRes makeGame(CreateGameReq gameReq) throws DataAccessException;

    public void joinGame(JoinGameReq gameReq, String username) throws DataAccessException;

    public void deleteAllGames();

    //OLD BELOW

    public int createGame(String gameName) throws DataAccessException;

    public Collection<GameData> getAllGames();

    public GameData getGame(int gameID) throws DataAccessException;

    public void joinGame(int gameID, ChessGame.TeamColor color, String username) throws DataAccessException;



}
