package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.MemoryGameDAO;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GameServiceTests {

    private final GameService service = new GameService(new MemoryGameDAO());

    @Test
    @DisplayName("create one game")
    public void createGoodGame() throws DataAccessException {
        Assertions.assertEquals(1, service.createGame("game"));
    }

    @Test
    @DisplayName("create two games same name")
    public void createSameGame() throws DataAccessException {
        Assertions.assertEquals(1, service.createGame("game"));
        Assertions.assertEquals(2, service.createGame("game"));
    }

    @Test
    @DisplayName("get games empty")
    public void getGamesEmpty() throws DataAccessException {
        Assertions.assertEquals(0, service.getAllGames().size());
    }

    @Test
    @DisplayName("get games")
    public void getGames() throws DataAccessException {
        service.createGame("mygame");
        Assertions.assertEquals(1, service.getAllGames().size());
    }

    @Test
    @DisplayName("get valid game")
    public void getGame() throws DataAccessException {
        service.createGame("mygame");
        Assertions.assertDoesNotThrow(() -> service.getGame(1));
    }

    @Test
    @DisplayName("get invalid game")
    public void getBadGame() throws DataAccessException {
        service.createGame("mygame");
        Assertions.assertThrows(DataAccessException.class, () -> service.getGame(2));
    }

    @Test
    @DisplayName("join good game")
    public void joinGoodGame() throws DataAccessException {
        service.createGame("mygame");
        Assertions.assertDoesNotThrow(() -> service.joinGame(1, ChessGame.TeamColor.WHITE, "myuser"));
    }

    @Test
    @DisplayName("join bad game")
    public void joinBadGame() throws DataAccessException {
        service.createGame("game");
        Assertions.assertThrows(DataAccessException.class, () -> service.joinGame(2, ChessGame.TeamColor.WHITE, "user"));
    }

    @Test
    @DisplayName("delete all")
    public void deleteAll() throws DataAccessException {
        service.createGame("game");
        service.deleteAll();
        Assertions.assertThrows(DataAccessException.class, () -> service.getGame(1));
    }
}
