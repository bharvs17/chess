package server;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.*;
import model.*;
import model.AuthData;
import service.AuthService;
import service.GameService;
import service.UserService;
import spark.*;
import exception.DataAccessException;
import websocket.WebSocketHandler;

import java.sql.SQLException;

public class Server {

    private final Gson gson;
    private final UserService userService;
    private final AuthService authService;
    private final GameService gameService;
    private final WebSocketHandler webSocketHandler;

    public Server() {
        this.gson = new Gson();
        try {
            this.userService = new UserService(new SQLUserDAO());
            this.authService = new AuthService(new SQLAuthDAO());
            this.gameService = new GameService(new SQLGameDAO());
            webSocketHandler = new WebSocketHandler(gameService,authService);
        } catch(DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
    //plus services for auth and game

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");
        // Register your endpoints and handle exceptions here.

        Spark.webSocket("/ws",webSocketHandler);

        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);
        Spark.delete("/db", this::clear);
        Spark.get("/game/:id", this::getGameData);
        Spark.put("/game/:id/:color", this::userLeave);
        Spark.put("/game/:id", this::updateGame);
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
        res.status(ex.statusCode());
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
        authService.checkAuth(authToken);
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

    public void clearAll(String password) {
        if(password.equals("password")) {
            userService.deleteAllUsers();
            authService.deleteAllAuths();
            gameService.deleteAllGames();
        }
    }

    private Object getGameData(Request req, Response res) throws DataAccessException {
        String authToken = req.headers("authorization");
        authService.checkAuth(authToken);
        int gameID;
        try {
            gameID = Integer.parseInt(req.params(":id"));
        } catch (Exception ex) {
            throw new DataAccessException(400, "Error: something went wrong with the game id");
        }
        ChessGame reqGame = gameService.getGame(gameID);
        res.status(200);
        return gson.toJson(reqGame);
    }

    private Object userLeave(Request req, Response res) throws DataAccessException {
        String authToken = req.headers("authorization");
        int gameID;
        authService.checkAuth(authToken);
        try {
            gameID = Integer.parseInt(req.params(":id"));
        } catch (Exception ex) {
            throw new DataAccessException(400, "Error: something went wrong with the game id");
        }
        String color = req.params(":color");
        gameService.removeUser(gameID,color);
        res.status(200);
        return "";
    }

    private Object updateGame(Request req, Response res) throws DataAccessException {
        String authToken = req.headers("authorization");
        int gameID;
        authService.checkAuth(authToken);
        try {
            gameID = Integer.parseInt(req.params(":id"));
        } catch (Exception ex) {
            throw new DataAccessException(400, "Error: something went wrong with the game id");
        }
        ChessGame game = gson.fromJson(req.body(), ChessGame.class);
        gameService.updateGame(gameID, game);
        res.status(200);
        return "";
    }

}
