package dataaccess;

import chess.ChessGame;
import model.CreateGameReq;
import model.CreateGameRes;
import model.JoinGameReq;
import model.ListGameRes;
import model.GameData;
import java.util.HashMap;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Map;
import exception.DataAccessException;

import model.GameInfo;

public class MemoryGameDAO implements GameDAO {

    final private HashMap<Integer, GameData> games = new HashMap<>();
    private int gameID = 1;

    @Override
    public ListGameRes listGames() throws DataAccessException {
        Collection<GameInfo> info = new ArrayList<>();
        for(Map.Entry<Integer,GameData> entry : games.entrySet()) {
            GameData val = entry.getValue();
            info.add(new GameInfo(val.gameID(),val.whiteUsername(), val.blackUsername(), val.gameName()));
        }
        return new ListGameRes(info);
    }

    @Override
    public CreateGameRes makeGame(CreateGameReq gameReq) throws DataAccessException {
        if (gameReq.gameName() == null) {
            throw new DataAccessException(400, "Error: bad request");
        }
        games.put(gameID, new GameData(gameID, null, null, gameReq.gameName(), new ChessGame()));
        gameID++;
        return new CreateGameRes(gameID-1);
    }

    @Override
    public void joinGame(JoinGameReq gameReq, String username) throws DataAccessException {
        ChessGame.TeamColor reqColor = gameReq.playerColor();
        int reqID = gameReq.gameID();
        if(reqID <= 0 || !(reqColor == ChessGame.TeamColor.BLACK || reqColor == ChessGame.TeamColor.WHITE)) {
            throw new DataAccessException(400, "Error: bad request");
        }
        if(!games.containsKey(reqID)) {
            throw new DataAccessException(401, "Error: unauthorized");
        }
        if(reqColor == ChessGame.TeamColor.WHITE) {
            if(games.get(reqID).whiteUsername() != null) {
                throw new DataAccessException(403, "Error: already taken");
            } else {
                String bUsername = games.get(reqID).blackUsername();
                String gName = games.get(reqID).gameName();
                GameData updatedGame = new GameData(reqID,username,bUsername,gName,games.get(reqID).game());
                games.remove(reqID);
                games.put(reqID,updatedGame);
            }
        } else {
            if(games.get(reqID).blackUsername() != null) {
                throw new DataAccessException(403, "Error: already taken");
            } else {
                String wUsername = games.get(reqID).whiteUsername();
                String gName = games.get(reqID).gameName();
                GameData updatedGame = new GameData(reqID,wUsername,username,gName,games.get(reqID).game());
                games.remove(reqID);
                games.put(reqID,updatedGame);
            }
        }
    }

    @Override
    public void deleteAllGames() throws DataAccessException {
        games.clear();
        gameID = 1;
    }

    @Override
    public ChessGame getGame(int gameID) throws DataAccessException {
        GameData data = games.get(gameID);
        if(data == null) {
            throw new DataAccessException(400, "Error: no such game id in database");
        }
        return data.game();
    }

    @Override
    public void removeUser(int gameID, String color) throws DataAccessException {
        String w = games.get(gameID).whiteUsername();
        String b = games.get(gameID).blackUsername();
        String name = games.get(gameID).gameName();
        ChessGame game = games.get(gameID).game();
        if(color.equals("white")) {
            if(w == null) {
                throw new DataAccessException(400, "Error: no player to remove at white");
            }
            games.put(gameID, new GameData(gameID,null,b,name,game));
        } else {
            if(b == null) {
                throw new DataAccessException(400, "Error: no player to remove at black");
            }
            games.put(gameID, new GameData(gameID,w,null,name,game));
        }
    }

    @Override
    public void updateGame(int gameID, ChessGame game) throws DataAccessException {
        GameData data = games.get(gameID);
        if(data == null) {
            throw new DataAccessException(400, "Error: the given game id was not found");
        }
        String white = games.get(gameID).whiteUsername();
        String gName = games.get(gameID).gameName();
        String black = games.get(gameID).blackUsername();
        games.put(gameID, new GameData(gameID,white,black,gName,game));
    }

    @Override
    public void resign(int gameID) throws DataAccessException {
        GameData data = games.get(gameID);
        if(data == null) {
            throw new DataAccessException(400, "Error: the given game id was not found");
        }
        ChessGame game = games.get(gameID).game();
        game.resign();
        updateGame(gameID, game);
    }

}
