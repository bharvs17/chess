package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.model.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;


public class SQLGameDAO implements GameDAO {

    private int gameIDCount = 1;
    private final Gson gson;

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  games (
              `id` int NOT NULL AUTO_INCREMENT,
              `gameID` int NOT NULL,
              `whiteusername` varchar(256) DEFAULT NULL,
              `blackusername` varchar(256) DEFAULT NULL,
              `gamename` varchar(256) NOT NULL,
              `chessgameJSON` TEXT DEFAULT NULL,
              PRIMARY KEY (`id`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            conn.setCatalog("chess");
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(500, String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }

    public SQLGameDAO() throws DataAccessException {
        configureDatabase();
        gson = new Gson();
    }

    @Override
    public ListGameRes listGames() throws DataAccessException {
        Collection<GameInfo> games = new ArrayList<>();
        try(var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM games";
            try(var ps = conn.prepareStatement(statement)) {
                try(var rs = ps.executeQuery()) {
                    while(rs.next()) {
                        int id = rs.getInt("gameID");
                        String whiteName = rs.getString("whiteusername");
                        String blackName = rs.getString("blackusername");
                        String gameName = rs.getString("gamename");
                        games.add(new GameInfo(id,whiteName,blackName,gameName));
                    }
                }
            }
        } catch(Exception e) {
            throw new DataAccessException(500, String.format("Unable to read data: %s%n",e.getMessage()));
        }
        return new ListGameRes(games);
    }

    @Override
    public CreateGameRes makeGame(CreateGameReq gameReq) throws DataAccessException {
        if (gameReq.gameName() == null) {
            throw new DataAccessException(400, "Error: bad request");
        }
        String name = gameReq.gameName();
        ChessGame game = new ChessGame();
        var gameJson = gson.toJson(game);
        try(var conn = DatabaseManager.getConnection()) {
            var statement = "INSERT INTO games (gameID, gamename, chessgameJSON) VALUES (?, ?, ?)";
            try(var ps = conn.prepareStatement(statement)) {
                ps.setInt(1,gameIDCount);
                gameIDCount++;
                ps.setString(2,name);
                ps.setString(3,gameJson);
                ps.executeUpdate();
            }
        } catch(Exception e) {
            throw new DataAccessException(500, String.format("Unable to read data: %s%n",e.getMessage()));
        }
        return new CreateGameRes(gameIDCount-1);
    }

    @Override
    public void joinGame(JoinGameReq gameReq, String username) throws DataAccessException {
        ChessGame.TeamColor reqColor = gameReq.playerColor();
        int reqID = gameReq.gameID();
        String wUser = null;
        String bUser = null;
        if(reqID <= 0 || !(reqColor == ChessGame.TeamColor.BLACK || reqColor == ChessGame.TeamColor.WHITE)) {
            throw new DataAccessException(400, "Error: bad request");
        }
        try(var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM games WHERE gameID = ?";
            try(var ps = conn.prepareStatement(statement)) {
                ps.setInt(1,reqID);
                try(var rs = ps.executeQuery()) {
                    if(rs.next()) {
                        wUser = rs.getString("whiteusername");
                        bUser = rs.getString("blackusername");
                    } else {
                        throw new DataAccessException(401, "Error: unauthorized");
                    }
                }
            }
        } catch(Exception e) {
            throw new DataAccessException(500, String.format("Unable to read data: %s%n",e.getMessage()));
        }
        if(reqColor == ChessGame.TeamColor.WHITE) {
            if(wUser != null) {
                throw new DataAccessException(403, "Error: already taken");
            } else {
                addUser(reqID,username,reqColor);
            }
        } else {
            if(bUser != null) {
                throw new DataAccessException(403, "Error: already taken");
            } else {
                addUser(reqID,username,reqColor);
            }
        }
    }

    private void addUser(int gameID, String username, ChessGame.TeamColor color) throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()) {
            String statement;
            if(color == ChessGame.TeamColor.WHITE) {
                statement = "UPDATE games SET whiteusername = ? WHERE gameID = ?";
            } else {
                statement = "UPDATE games SET blackusername = ? WHERE gameID = ?";
            }
            try(var ps = conn.prepareStatement(statement)) {
                ps.setString(1,username);
                ps.setInt(2,gameID);
                ps.executeUpdate();
            }
        } catch(Exception e) {
            throw new DataAccessException(500, String.format("Unable to read data: %s%n",e.getMessage()));
        }
    }

    @Override
    public void deleteAllGames() throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()) {
            var statement = "TRUNCATE games";
            try(var ps = conn.prepareStatement(statement)) {
                ps.executeUpdate();
            }
        } catch(Exception e) {
            throw new DataAccessException(401, String.format("Unable to perform request: %s%n",e.getMessage()));
        }
    }

}
