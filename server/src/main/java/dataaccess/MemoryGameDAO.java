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
    }

}
