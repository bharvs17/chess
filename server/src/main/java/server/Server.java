package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;
import service.AuthService;
import service.GameService;
import service.UserService;
import spark.*;

import java.util.UUID;

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
        Spark.post("/user", this::register); //register
        Spark.post("/name", this::test); //TEST FUNCTION
        //Spark.post("/session", ) //login
        //Spark.delete("/session", ) //logout
        //Spark.get("/game", ) //listGames
        //Spark.post("/game", ) //createGame
        //Spark.put("/game", ) //joinGame
        //Spark.delete("/db", ) //clear
        Spark.exception(DataAccessException.class, this::exceptionHandler);

        //2nd parameter is function to link to
        //could do in a handler class or in here
        //see petshop for example
        //still need to figure out database

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object test(Request req, Response res) {
        String h = "hello";
        return gson.toJson(h);
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



}
