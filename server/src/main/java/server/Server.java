package server;

import spark.*;
import handler.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        createRoutes();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private void createRoutes(){
        Spark.webSocket("/connect", WebSocketServer.class);

        //Clear App
        Spark.delete("/db",(req,res) -> new ClearHandler().cleardb(req,res));
        //Register
        Spark.post("/user", (req,res) -> new RegisterHandler().handle(req, res));
        //Login
        Spark.post("/session", (req,res) -> new LoginHandler().handle(req, res));
        //Logout
        Spark.delete("/session", (req,res)-> new LogoutHandler().handle(req, res));
        // List games
        Spark.get("/game",(req,res) -> new ListGamesHandler().handle(req, res));
        //Create game
        Spark.post("/game",(req,res) -> new CreateGameHandler().handle(req, res));
        //Join game
        Spark.put("/game", (req,res) -> new JoinGameHandler().handle(req, res));

    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
