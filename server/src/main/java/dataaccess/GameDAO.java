package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;

public interface GameDAO {

    public int createGame(String gameName) throws DataAccessException;

    public Collection<GameData> getAllGames();

    public GameData getGame(int gameID) throws DataAccessException;

    public void joinGame(int gameID, ChessGame.TeamColor color, String username) throws DataAccessException;

    public void deleteAll();

}
