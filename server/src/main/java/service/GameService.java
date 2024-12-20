package service;

import chess.ChessGame;
import exception.DataAccessException;
import dataaccess.GameDAO;
import model.CreateGameReq;
import model.CreateGameRes;
import model.JoinGameReq;
import model.ListGameRes;

public class GameService {

    private final GameDAO gameAccess;

    public GameService(GameDAO gameAccess) {
        this.gameAccess = gameAccess;
    }

    public ListGameRes listGames() {
        try {
            return gameAccess.listGames();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public CreateGameRes makeGame(CreateGameReq gameReq) throws DataAccessException {
        return gameAccess.makeGame(gameReq);
    }

    public void joinGame(JoinGameReq gameReq, String username) throws DataAccessException {
        gameAccess.joinGame(gameReq, username);
    }

    public void deleteAllGames() {
        try {
            gameAccess.deleteAllGames();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public ChessGame getGame(int gameID) throws DataAccessException {
        return gameAccess.getGame(gameID);
    }

    public void removeUser(int gameID, String color) throws DataAccessException {
        gameAccess.removeUser(gameID, color);
    }

    public void updateGame(int gameID, ChessGame game) throws DataAccessException {
        gameAccess.updateGame(gameID, game);
    }

    public ChessGame.TeamColor getPlayerColor(int gameID, String username) throws DataAccessException {
        return gameAccess.getPlayerColor(gameID, username);
    }

}
