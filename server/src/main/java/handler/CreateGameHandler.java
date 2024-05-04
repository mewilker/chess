package handler;

import spark.*;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import service.CreateGameService;
import request.CreateGameRequest;
import result.CreateGameResult;
import result.Result;

public class CreateGameHandler {
    public Object handle(Request req, Response res){
        Gson gson = new Gson();
        CreateGameRequest request;
        try{ //may need a type adapter
            request = gson.fromJson(req.body(), CreateGameRequest.class);
            request.setAuthToken(req.headers("Authorization"));
        }
        catch(JsonSyntaxException e){
            System.out.println("a quote unquote bad request was made");
            request = null;
        }
        CreateGameResult result = new CreateGameService().createGame(request);
        if (result.getMessage() != null){
            Result message = new Result();
            message.requestError();
            if (message.getMessage().equals(result.getMessage())){
                res.status(400);
                return gson.toJson(message);
            }
            else{
                message.authError();
                if(message.getMessage().equals(result.getMessage())){
                    res.status(401);
                    return gson.toJson(message);
                }
                else {
                    res.status(500);
                }
            }
        }
        return gson.toJson(result);
    }
}
