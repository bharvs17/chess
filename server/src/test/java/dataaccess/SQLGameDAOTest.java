package dataaccess;

import chess.ChessGame;
import exception.DataAccessException;
import model.CreateGameReq;
import model.JoinGameReq;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SQLGameDAOTest {

    SQLGameDAO gdao;

    public SQLGameDAOTest() throws DataAccessException {
        gdao = new SQLGameDAO();
    }

    @BeforeEach
    public void clear() throws DataAccessException {
        gdao.deleteAllGames();
    }

    @Test
    void listGamesEmpty() throws DataAccessException {
        Assertions.assertTrue(gdao.listGames().games().isEmpty());
    }

    @Test
    void listGamesGood() throws DataAccessException {
        gdao.makeGame(new CreateGameReq("bgame"));
        Assertions.assertEquals(1, gdao.listGames().games().size());
    }

    @Test
    void makeGoodGame() {
        Assertions.assertDoesNotThrow(() -> gdao.makeGame(new CreateGameReq("bgame")));
    }

    @Test
    void makeBadGame() {
        String name = null;
        Assertions.assertThrows(DataAccessException.class, () -> gdao.makeGame(new CreateGameReq(name)));
    }

    @Test
    void joinGoodGame() throws DataAccessException {
        gdao.makeGame(new CreateGameReq("game1"));
        Assertions.assertDoesNotThrow(() -> gdao.joinGame(new JoinGameReq(ChessGame.TeamColor.WHITE,1000),"user"));
    }

    @Test
    void joinTakenGame() throws DataAccessException {
        gdao.makeGame(new CreateGameReq("game1"));
        gdao.joinGame(new JoinGameReq(ChessGame.TeamColor.WHITE,1000),"user1");
        Assertions.assertThrows(DataAccessException.class, () -> gdao.joinGame(new JoinGameReq(ChessGame.TeamColor.WHITE,1000),"user2"));
    }

    @Test
    void deleteAllGames() throws DataAccessException {
        gdao.makeGame(new CreateGameReq("game1"));
        Assertions.assertDoesNotThrow(() -> gdao.deleteAllGames());
    }

    @Test
    void getGameBad() throws DataAccessException {
        gdao.makeGame(new CreateGameReq("game"));
        Assertions.assertThrows(DataAccessException.class, () -> gdao.getGame(0));
    }

    @Test
    void getGameGood() throws DataAccessException {
        gdao.makeGame(new CreateGameReq("game"));
        Assertions.assertDoesNotThrow(() -> gdao.getGame(1000));
    }

    @Test
    void removeUserBad() throws DataAccessException {
        gdao.makeGame(new CreateGameReq("game"));
        Assertions.assertThrows(DataAccessException.class, () -> gdao.removeUser(0, "white"));
    }

    @Test
    void removeUserGood() throws DataAccessException {
        gdao.makeGame(new CreateGameReq("game"));
        Assertions.assertDoesNotThrow(() -> gdao.removeUser(1000, "white"));
    }

    @Test
    void updateGameBad() throws DataAccessException {
        gdao.makeGame(new CreateGameReq("game"));
        Assertions.assertThrows(DataAccessException.class, () -> gdao.updateGame(0, new ChessGame()));
    }

    @Test
    void updateGameGood() throws DataAccessException {
        gdao.makeGame(new CreateGameReq("game"));
        Assertions.assertDoesNotThrow(() -> gdao.updateGame(1000, new ChessGame()));
    }

}