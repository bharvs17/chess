package client;

import chess.ChessGame;
import exception.DataAccessException;
import model.CreateGameReq;
import model.JoinGameReq;
import model.LoginReq;
import model.RegisterReq;
import org.junit.jupiter.api.*;
import server.Server;
import clientfiles.ServerFacade;

public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        String serverUrl = String.format("http://localhost:%d", port);
        facade = new ServerFacade(serverUrl);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    public void clear() {
        server.clearAll("password");
    }

    @Test
    public void registerInvalidTaken() throws DataAccessException {
        RegisterReq goodReq = new RegisterReq("username","pass","email");
        RegisterReq badReq = new RegisterReq("username","basdf","email.com");
        facade.register(goodReq);
        Assertions.assertThrows(DataAccessException.class, () -> facade.register(badReq));
    }

    @Test
    public void registerValid() throws DataAccessException {
        RegisterReq goodReq = new RegisterReq("username","pass","email");
        RegisterReq alsoGood = new RegisterReq("user2","diffpass","email2");
        facade.register(goodReq);
        Assertions.assertDoesNotThrow(() -> facade.register(alsoGood));
    }

    @Test
    public void loginInvalid() throws DataAccessException {
        facade.register(new RegisterReq("user","pass","email"));
        LoginReq badLogin = new LoginReq("user","pass1");
        Assertions.assertThrows(DataAccessException.class, () -> facade.login(badLogin));
    }

    @Test
    public void loginValid() throws DataAccessException {
        RegisterReq reg = new RegisterReq("user","pass","email");
        LoginReq goodLogin = new LoginReq("user","pass");
        facade.register(reg);
        Assertions.assertDoesNotThrow(() -> facade.login(goodLogin));
    }

    @Test
    public void logoutInvalid() throws DataAccessException {
        Assertions.assertThrows(DataAccessException.class, () -> facade.logout());
    }

    @Test
    public void logoutValid() throws DataAccessException {
        facade.register(new RegisterReq("user","pass","email"));
        Assertions.assertDoesNotThrow(() -> facade.logout());
    }

    @Test
    public void listGamesInvalid() throws DataAccessException {
        Assertions.assertThrows(DataAccessException.class, () -> facade.listGames());
    }

    @Test
    public void listGamesValid() throws DataAccessException {
        facade.register(new RegisterReq("user","pass","email"));
        Assertions.assertDoesNotThrow(() -> facade.listGames());
    }

    @Test
    public void createGameInalid() throws DataAccessException {
        Assertions.assertThrows(DataAccessException.class, () -> facade.createGame(new CreateGameReq("mygame")));
    }

    @Test
    public void createGameValid() throws DataAccessException {
        facade.register(new RegisterReq("user","pass","email"));
        Assertions.assertDoesNotThrow(() -> facade.createGame(new CreateGameReq("mygame")));
    }

    @Test
    public void joinGameInvalid() throws DataAccessException {
        facade.register(new RegisterReq("user","pass","email"));
        facade.createGame(new CreateGameReq("mygame"));
        Assertions.assertThrows(DataAccessException.class, () -> facade.joinGame(new JoinGameReq(ChessGame.TeamColor.WHITE,5)));
    }

    @Test
    public void joinGameValid() throws DataAccessException {
        facade.register(new RegisterReq("user","pass","email"));
        facade.createGame(new CreateGameReq("mygame"));
        Assertions.assertDoesNotThrow(() -> facade.joinGame(new JoinGameReq(ChessGame.TeamColor.WHITE,1)));
    }

    @Test
    public void deleteAllInvalid() throws DataAccessException {
        facade.register(new RegisterReq("user","pass","email"));
        facade.createGame(new CreateGameReq("game"));
        facade.logout();
        facade.deleteAll();
        Assertions.assertThrows(DataAccessException.class, () -> facade.login(new LoginReq("user","pass")));
    }

    @Test
    public void deleteAllValid() throws DataAccessException {
        facade.register(new RegisterReq("user","pass","email"));
        facade.createGame(new CreateGameReq("game"));
        facade.logout();
        Assertions.assertDoesNotThrow(() -> facade.deleteAll());
    }

}
