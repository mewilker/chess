package handler;

import spark.*;
import com.google.gson.Gson;

import service.ClearApp;
import result.Result;

public class ClearHandler {  
    
    ClearApp service = new ClearApp();

    public Object cleardb(Request req, Response res){
        Result result = service.clearApp();
        if (result.getMessage() == null){
            res.status(200);
            //System.out.println("success!");
        }
        else{
            res.status(500);
        }
        res.type("application/json");
        var body = new Gson().toJson(result);
        res.body(body);
        //System.out.println("failure!");
        return body;
    }
}
