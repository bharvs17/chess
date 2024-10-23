package dataaccess;

import chess.ChessGame;
import dataaccess.model.CreateGameReq;
import dataaccess.model.CreateGameRes;
import dataaccess.model.JoinGameReq;
import dataaccess.model.ListGameRes;
import model.GameData;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {

    final private HashMap<Integer, GameData> games = new HashMap<>();
    private int gameID = 1;

    @Override
    public ListGameRes listGames() {
        return new ListGameRes(games.values());
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

}
