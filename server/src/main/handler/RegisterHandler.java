package handler;

import spark.*;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import service.RegisterService;
import request.RegisterRequest;
import result.RegisterResult;

public class RegisterHandler {
    private RegisterService serve = new RegisterService();
    
    public Object handle(Request req, Response res){
        Gson gson = new Gson();
        RegisterRequest request;
        try{
            request = gson.fromJson(req.body(), RegisterRequest.class);
        }
        catch (JsonSyntaxException e){
            request = null;
        }
        RegisterResult result = serve.register(request);
        if (result.getMessage() != null){
            RegisterResult messages = new RegisterResult();
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
                    res.status(500);
                }
            }
        }
        return gson.toJson(result);
    }

}
