package handler;

import spark.*;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import service.JoinGameService;
import result.Result;
import request.JoinGameRequest;

public class JoinGameHandler {
    public Object handle (Request req, Response res){
        Gson gson = new Gson();
        JoinGameRequest request;
        try{
            request = gson.fromJson(req.body(), JoinGameRequest.class);
            request.setAuthToken(req.headers("Authorization"));
        }
        catch(JsonSyntaxException e){
            request = null;
        }
        Result result = new JoinGameService().joinGame(request);
        if (result.getMessage() != null){
            Result messages = new Result();
            messages.requestError();
            if (result.getMessage().equals(messages.getMessage())){
                res.status(400);
            }
            else{
                messages.takenError();
                if (result.getMessage().equals(messages.getMessage())){
                    res.status(403);
                }
                else{
                    messages.authError();
                    if (result.getMessage().equals(messages.getMessage())){
                        res.status(401);
                    }
                    else{
                        res.status(500);
                    }
                }
            }
        }
        return gson.toJson(result);
    }

}
