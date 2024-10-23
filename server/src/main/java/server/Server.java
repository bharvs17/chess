package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.model.CreateGameReq;
import dataaccess.model.JoinGameReq;
import dataaccess.model.LoginReq;
import dataaccess.model.RegisterReq;
import model.AuthData;
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
        Spark.post("/game", this::createGame);
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
        /*UserData userData = gson.fromJson(req.body(), UserData.class);
        var check = userService.checkUser(userData.username());
        if(check != null) {
            throw new DataAccessException(500, "Username already exists in database.");
        } else {
            AuthData authData = userService.createUser(userData);
            authService.createAuth(authData);
            res.status(200);
            return gson.toJson(authData);
        }*/
        RegisterReq registerReq = gson.fromJson(req.body(), RegisterReq.class);
        AuthData authData = userService.registerUser(registerReq);
        authService.addAuth(authData);
        res.status(200);
        return gson.toJson(authData);
    }

    private Object login(Request req, Response res) throws DataAccessException {
        /*UserData userData = gson.fromJson(req.body(), UserData.class);
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
        }*/
        LoginReq loginReq = gson.fromJson(req.body(), LoginReq.class);
        AuthData authData = userService.loginUser(loginReq);
        authService.addAuth(authData);
        res.status(200);
        return gson.toJson(authService.getAuth(loginReq.username()));
    }

    private Object logout(Request req, Response res) throws DataAccessException {
        /*String authToken = req.headers("authorization:");
        if(!authService.tokenInDatabase(authToken)) {
            throw new DataAccessException(500, "No such auth token in database.");
        } else {
            authService.deleteAuth(authToken);
            res.status(200);
            return "";
        }*/
        String authToken = req.headers("authorization:");
        authService.logout(authToken);
        res.status(200);
        return "";
    }

    private Object listGames(Request req, Response res) throws DataAccessException {
        /*String authToken = req.headers("authorization:");
        if(!authService.tokenInDatabase(authToken)) {
            throw new DataAccessException(500, "No such auth token in database.");
        } else {
            res.status(200);
            return gson.toJson(gameService.getAllGames());
        }*/
        String authToken = req.headers("authorization:");
        authService.checkAuth(authToken);
        res.status(200);
        return gson.toJson(gameService.listGames());
    }

    private Object createGame(Request req, Response res) throws DataAccessException {
        /*String authToken = req.headers("authorization:");
        String gameName = req.body();
        if(!authService.tokenInDatabase(authToken)) {
            throw new DataAccessException(500, "No such auth token in database.");
        } else {
            res.status(200);
            return gson.toJson(gameService.createGame(gameName));
        }*/
        String authToken = req.headers("authorization:");
        authService.checkAuth(authToken);
        CreateGameReq gameReq = gson.fromJson(req.body(), CreateGameReq.class);
        res.status(200);
        return gson.toJson(gameService.makeGame(gameReq));
    }

    private Object joinGame(Request req, Response res) throws DataAccessException {
        /*String authToken = req.headers("authorization:");
        JoinGameReq joinGameReq = gson.fromJson(req.body(), JoinGameReq.class);
        if(!authService.tokenInDatabase(authToken)) {
            throw new DataAccessException(500, "No such auth token in database.");
        } else {
            gameService.getGame(joinGameReq.gameID());
            res.status(200);
            gameService.joinGame(joinGameReq.gameID(),joinGameReq.playerColor(),authService.getAuth(authToken).username());
        }
        return "";*/
        String authToken = req.headers("authorization:");
        authService.checkAuth(authToken);
        //get username from authtoken, add it as a parameter to the joinGame method
        JoinGameReq gameReq = gson.fromJson(req.body(), JoinGameReq.class);
        gameService.joinGame(gameReq, authService.getUsername(authToken));
        res.status(200);
        return "";
    }

    private Object clear(Request req, Response res) throws DataAccessException {
        userService.deleteAllUsers();
        authService.deleteAllAuths();
        gameService.deleteAllGames();
        return "";
    }

}
