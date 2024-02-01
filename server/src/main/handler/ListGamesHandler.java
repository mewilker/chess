package handler;

import spark.*;
import com.google.gson.Gson;

import service.ListGamesService;
import request.AuthorizedRequest;
import result.ListGamesResult;

public class ListGamesHandler {
    public Object handle (Request req, Response res){
        AuthorizedRequest request = new AuthorizedRequest(req.headers("Authorization"));
        ListGamesResult result = new ListGamesService().listGames(request);
        if (result.getMessage() != null){
            ListGamesResult message = new ListGamesResult();
            message.authError();
            if (message.getMessage().equals(result.getMessage())){
                res.status(401);
            }
            else {
                res.status(500);
            }
        }
        Gson gson = new Gson();
        return gson.toJson(result);
    }
}
