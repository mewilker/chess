package handler;


import spark.*;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import service.LogoutService;
import request.AuthorizedRequest;
import result.Result;

public class LogoutHandler {
    public Object handle (Request req, Response res){
        Gson gson = new Gson();
        AuthorizedRequest request;
        try{
            //System.out.println(req.headers("Authorization"));
            request = new AuthorizedRequest(req.headers("Authorization"));
        }
        catch (JsonSyntaxException e){
            //System.out.println("authorization header still didn't work");
            return null;
        }
        Result result = new LogoutService().logout(request);
        if (result.getMessage() != null){
            Result message = new Result();
            message.authError();
            if (message.getMessage().equals(result.getMessage())){
                res.status(401);
            }
            else {
                res.status(500);
            }
        }
        return gson.toJson(result);
    }
}
