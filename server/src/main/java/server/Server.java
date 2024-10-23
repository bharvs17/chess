package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;
import model.JoinGameReq;
import service.AuthService;
import service.GameService;
import service.UserService;
import spark.*;
import java.util.ArrayList;

public class Server {

    private final Gson gson;
    private final UserService userService;
    private final AuthService authService;
    private final GameService gameService;
    private ArrayList<String> strings = new ArrayList<>();

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
        Spark.post("/game/", this::createGame);
        Spark.put("/game", this::joinGame);
        Spark.delete("/db", this::clear);
        Spark.exception(DataAccessException.class, this::exceptionHandler);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private void exceptionHandler(DataAccessException ex, Request req, Response res) {
        res.status(ex.StatusCode());
    }

    private Object register(Request req, Response res) throws DataAccessException {
        UserData userData = gson.fromJson(req.body(), UserData.class);
        var check = userService.checkUser(userData.username());
        if(check != null) {
            throw new DataAccessException(500, "Username already exists in database.");
        } else {
            AuthData authData = userService.createUser(userData);
            authService.createAuth(authData);
            res.status(200);
            return gson.toJson(authData);
        }
    }

    private Object login(Request req, Response res) throws DataAccessException {
        UserData userData = gson.fromJson(req.body(), UserData.class);
        userData = userService.checkUser(userData.username());
        if(userData == null) {
            throw new DataAccessException(500, "No such username in database");
        } else {
            if(userService.getUser(userData.username(),userData.password()) == null) {
                throw new DataAccessException(500, "Incorrect password.");
            }
            AuthData authData = userService.createUser(userData);
            authService.createAuth(authData);
            res.status(200);
            return gson.toJson(authData);
        }
    }

    private Object logout(Request req, Response res) throws DataAccessException {
        String authToken = req.headers("authorization:");
        if(!authService.tokenInDatabase(authToken)) {
            throw new DataAccessException(500, "No such auth token in database.");
        } else {
            authService.deleteAuth(authToken);
            res.status(200);
            return "";
        }
    }

    private Object listGames(Request req, Response res) throws DataAccessException {
        String authToken = req.headers("authorization:");
        if(!authService.tokenInDatabase(authToken)) {
            throw new DataAccessException(500, "No such auth token in database.");
        } else {
            res.status(200);
            return gson.toJson(gameService.getAllGames());
        }
    }

    private Object createGame(Request req, Response res) throws DataAccessException {
        String authToken = req.headers("authorization:");
        String gameName = req.body();
        if(!authService.tokenInDatabase(authToken)) {
            throw new DataAccessException(500, "No such auth token in database.");
        } else {
            res.status(200);
            return gson.toJson(gameService.createGame(gameName));
        }
    }

    private Object joinGame(Request req, Response res) throws DataAccessException {
        String authToken = req.headers("authorization:");
        JoinGameReq joinGameReq = gson.fromJson(req.body(), JoinGameReq.class);
        if(!authService.tokenInDatabase(authToken)) {
            throw new DataAccessException(500, "No such auth token in database.");
        } else {
            gameService.getGame(joinGameReq.gameID());
            res.status(200);
            gameService.joinGame(joinGameReq.gameID(),joinGameReq.playerColor(),authService.getAuth(authToken).username());
        }
        return "";
    }

    private Object clear(Request req, Response res) throws DataAccessException {
        userService.deleteAllUsers();
        authService.deleteAllAuths();
        gameService.deleteAll();
        return "";
    }

}
