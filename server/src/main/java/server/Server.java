package server;

import service.UserService;
import spark.*;

public class Server {

    private final UserService userService;
    //plus services for auth and game

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", ); //register
        //Spark.post("/session", ) //login
        //Spark.delete("/session", ) //logout
        //Spark.get("/game", ) //listGames
        //Spark.post("/game", ) //createGame
        //Spark.put("/game", ) //joinGame
        //Spark.delete("/db", ) //clear

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
}
