package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import model.UserData;
import service.UserService;
import spark.*;

public class Server {

    private final Gson gson;
    private final UserService userService;

    public Server() {
        this.gson = new Gson();
        this.userService = new UserService(new MemoryUserDAO());
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
        var user = gson.fromJson(req.body(), UserData.class);
        var auth = userService.createUser(user);
        res.status(200);
        return gson.toJson(auth);
    }

}
