package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.MemoryGameDAO;
import model.CreateGameReq;
import model.GameInfo;
import model.JoinGameReq;
import model.ListGameRes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class GameServiceTest {

    private GameService service = new GameService(new MemoryGameDAO());

    @Test
    void listGamesEmpty() {
        Collection<GameInfo> emptyCollection = new ArrayList<>();
        ListGameRes emptyGames = new ListGameRes(emptyCollection);
        Assertions.assertEquals(emptyGames, service.listGames());
    }

    @Test
    void listGames() throws DataAccessException {
        service.makeGame(new CreateGameReq("myGame"));
        service.makeGame(new CreateGameReq("also a game"));
        Collection<GameInfo> games = new ArrayList<>();
        //add 2 games with same name and list, see if equal
        games.add(new GameInfo(1,null,null,"myGame"));
        games.add(new GameInfo(2,null,null,"also a game"));
        ListGameRes expected = new ListGameRes(games);
        Assertions.assertEquals(service.listGames(),expected);
    }

    @Test
    void makeGoodGame() throws DataAccessException {
        Assertions.assertDoesNotThrow(() -> service.makeGame(new CreateGameReq("new game")));
    }

    @Test
    void makeBadGame() throws DataAccessException {
        Assertions.assertThrows(DataAccessException.class, () -> service.makeGame(new CreateGameReq(null)));
    }

    @Test
    void joinGoodGame() throws DataAccessException {
        service.makeGame(new CreateGameReq("new game"));
        Assertions.assertDoesNotThrow(() -> service.joinGame(new JoinGameReq(ChessGame.TeamColor.WHITE,1),"user1"));
    }

    @Test
    void joinGameTaken() throws DataAccessException {
        service.makeGame(new CreateGameReq("another game"));
        service.joinGame(new JoinGameReq(ChessGame.TeamColor.WHITE,1),"user1");
        Assertions.assertThrows(DataAccessException.class, () -> service.joinGame(new JoinGameReq(ChessGame.TeamColor.WHITE,1),"user2"));
    }

    @Test
    void deleteAllGames() throws DataAccessException {
        service.makeGame(new CreateGameReq("game1"));
        service.makeGame(new CreateGameReq("game2"));
        service.deleteAllGames();
        Collection<GameInfo> emptyGames = new ArrayList<>();
        ListGameRes expected = new ListGameRes(emptyGames);
        Assertions.assertEquals(service.listGames(), expected);
    }
}