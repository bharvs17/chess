package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class MemoryGameDAO implements GameDAO {

    final private HashMap<Integer, GameData> games = new HashMap<>();
    private int gameID = 1;

    @Override
    public int createGame(String gameName) throws DataAccessException {
        games.put(gameID, new GameData(gameID, null, null, gameName, new ChessGame()));
        gameID++;
        return gameID-1;
    }

    @Override
    public Collection<GameData> getAllGames() {
        return games.values();
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        if(!games.containsKey(gameID)) {
            throw new DataAccessException(500, "No such game exists.");
        } else {
            return games.get(gameID);
        }
    }

    @Override
    public void joinGame(int gameID, ChessGame.TeamColor color, String username) throws DataAccessException {
        if(!games.containsKey(gameID)) {
            throw new DataAccessException(500, "No such game exists.");
        }
        if(color == ChessGame.TeamColor.BLACK) {
            if(games.get(gameID).blackUsername() != null) {
                throw new DataAccessException(500, "Black player is already taken.");
            } else {
                GameData data = games.get(gameID);
                games.remove(gameID);
                games.put(gameID,new GameData(gameID,data.whiteUsername(),username,data.gameName(),data.game()));
            }
        } else {
            if(games.get(gameID).whiteUsername() != null) {
                throw new DataAccessException(500, "White player is already taken.");
            } else {
                GameData data = games.get(gameID);
                games.remove(gameID);
                games.put(gameID,new GameData(gameID,username,data.blackUsername(),data.gameName(),data.game()));
            }
        }

    }

    @Override
    public void deleteAll() {
        games.clear();
    }

}
