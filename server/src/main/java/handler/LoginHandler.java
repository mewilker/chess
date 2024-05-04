package handler;

import spark.*;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import service.LoginService;
import request.LoginRequest;
import result.LoginResult;

public class LoginHandler {
    public Object handle(Request req, Response res){
        Gson gson = new Gson();
        LoginRequest request;
        try{
            request = gson.fromJson(req.body(), LoginRequest.class);
        }
        catch(JsonSyntaxException e){
            request = null;
        }

        LoginResult result = new LoginService().login(request);
        if (result.getMessage() != null){
            LoginResult message = new LoginResult();
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
