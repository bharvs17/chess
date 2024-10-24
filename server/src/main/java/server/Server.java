package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.model.*;
import model.AuthData;
import service.AuthService;
import service.GameService;
import service.UserService;
import spark.*;

public class Server {

    private final Gson gson;
    private final UserService userService;
    private final AuthService authService;
    private final GameService gameService;

    public Server() {
        this.gson = new Gson();
        this.userService = new UserService(new MemoryUserDAO());
        this.authService = new AuthService(new MemoryAuthDAO());
        this.gameService = new GameService(new MemoryGameDAO());
    }
    //plus services for auth and game

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");
        // Register your endpoints and handle exceptions here.

        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);
        Spark.delete("/db", this::clear);
        Spark.exception(DataAccessException.class, this::exceptionHandler);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        //Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object exceptionHandler(DataAccessException ex, Request req, Response res) {
        res.status(ex.StatusCode());
        res.type("application/json");
        var body = gson.toJson(new ErrorMsg(ex.getMessage()));
        res.body(body);
        return body;
    }

    private Object register(Request req, Response res) throws DataAccessException {
        RegisterReq registerReq = gson.fromJson(req.body(), RegisterReq.class);
        AuthData authData = userService.registerUser(registerReq);
        authService.addAuth(authData);
        res.status(200);
        return gson.toJson(authData);
    }

    private Object login(Request req, Response res) throws DataAccessException {
        LoginReq loginReq = gson.fromJson(req.body(), LoginReq.class);
        AuthData authData = userService.loginUser(loginReq);
        authService.addAuth(authData);
        res.status(200);
        return gson.toJson(authData);
    }

    private Object logout(Request req, Response res) throws DataAccessException {
        String authToken = req.headers("authorization");
        authService.logout(authToken);
        res.status(200);
        return "";
    }

    private Object listGames(Request req, Response res) throws DataAccessException {
        String authToken = req.headers("authorization");
        authService.checkAuth(authToken);
        res.status(200);
        return gson.toJson(gameService.listGames());
    }

    private Object createGame(Request req, Response res) throws DataAccessException {
        String authToken = req.headers("authorization");
        System.out.printf("auth token here is %s%n", authToken);
        authService.checkAuth(authToken); //this is problem
        CreateGameReq gameReq = gson.fromJson(req.body(), CreateGameReq.class);
        res.status(200);
        return gson.toJson(gameService.makeGame(gameReq));
    }

    private Object joinGame(Request req, Response res) throws DataAccessException {
        String authToken = req.headers("authorization");
        authService.checkAuth(authToken);
        JoinGameReq gameReq = gson.fromJson(req.body(), JoinGameReq.class);
        gameService.joinGame(gameReq, authService.getUsername(authToken));
        res.status(200);
        return "";
    }

    private Object clear(Request req, Response res) {
        userService.deleteAllUsers();
        authService.deleteAllAuths();
        gameService.deleteAllGames();
        return "";
    }

}
