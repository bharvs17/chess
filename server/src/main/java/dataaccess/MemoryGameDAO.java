package dataaccess;

import chess.ChessGame;
import dataaccess.model.CreateGameReq;
import dataaccess.model.CreateGameRes;
import dataaccess.model.JoinGameReq;
import dataaccess.model.ListGameRes;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryGameDAO implements GameDAO {

    final private HashMap<Integer, GameData> games = new HashMap<>();
    private int gameID = 1;

    public ListGameRes listGames() {
        return new ListGameRes(games.values());
    }

    public CreateGameRes makeGame(CreateGameReq gameReq) throws DataAccessException {
        if (gameReq.gameName() == null) {
            throw new DataAccessException(400, "Error: bad request");
        }
        games.put(gameID, new GameData(gameID, null, null, gameReq.gameName(), new ChessGame()));
        gameID++;
        return new CreateGameRes(gameID-1);
    }

    public void joinGame(JoinGameReq gameReq, String username) throws DataAccessException {
        if(gameReq.gameID() <= 0 || !(gameReq.playerColor() == ChessGame.TeamColor.BLACK || gameReq.playerColor() == ChessGame.TeamColor.WHITE)) {
            throw new DataAccessException(400, "Error: bad request");
        }
        if(!games.containsKey(gameReq.gameID())) {
            throw new DataAccessException(401, "Error: unauthorized");
        }
        if(gameReq.playerColor() == ChessGame.TeamColor.WHITE) {
            if(games.get(gameReq.gameID()).whiteUsername() != null) {
                throw new DataAccessException(403, "Error: already taken");
            } else {
                GameData updatedGame = new GameData(gameReq.gameID(),username,games.get(gameReq.gameID()).blackUsername(),games.get(gameReq.gameID()).gameName(),games.get(gameReq.gameID()).game());
                games.remove(gameReq.gameID());
                games.put(gameReq.gameID(),updatedGame);
            }
        } else {
            if(games.get(gameReq.gameID()).blackUsername() != null) {
                throw new DataAccessException(403, "Error: already taken");
            } else {
                GameData updatedGame = new GameData(gameReq.gameID(),games.get(gameReq.gameID()).whiteUsername(),username,games.get(gameReq.gameID()).gameName(),games.get(gameReq.gameID()).game());
                games.remove(gameReq.gameID());
                games.put(gameReq.gameID(),updatedGame);
            }
        }
    }

    @Override
    public void deleteAllGames() {
        games.clear();
    }

    //OLD BELOW

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

}
