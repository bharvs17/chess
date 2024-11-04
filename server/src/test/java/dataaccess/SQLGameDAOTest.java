package dataaccess;

import dataaccess.model.CreateGameReq;
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
        String name = null;
        Assertions.assertThrows(DataAccessException.class, () -> gdao.makeGame(new CreateGameReq(name)));
    }

    @Test
    void makeBadGame() {

    }

    @Test
    void joinGame() {
    }

    @Test
    void deleteAllGames() {
    }
}