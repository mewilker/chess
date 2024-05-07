package serverfacade;

import java.io.*;
import java.net.*;

import chess.ChessPiece;
import com.google.gson.Gson;

import com.google.gson.GsonBuilder;
import result.Result;

public abstract class HttpCommunicator{

    private URI uri;
    private HttpURLConnection http;
    protected static final String CONNECTION_ERROR_MSG = "Something went wrong connecting to "+
        "the server. Please restart the program, specify a URL in the command-line arguments, "+
        "and try again.\nIf this message persists, please try again later.\n";

    protected HttpCommunicator(String url) throws URISyntaxException{
        uri = new URI(url);
    }

    protected void endpoint (String url) throws URISyntaxException{
        uri = new URI(url);
    }

    protected void initHTTP(String method) throws IOException{
        http = (HttpURLConnection) uri.toURL().openConnection();
        http.setReadTimeout(5000);
        http.setRequestMethod(method);
        if (method != "GET"){
            http.setDoOutput(true);
        }
    }

    protected void writeBody(Object request) throws IOException{
        http.addRequestProperty("Content-Type", "application/json");
        try(var outputStream = http.getOutputStream()){
            var body = new Gson().toJson(request);
            outputStream.write(body.getBytes());
        }
    }

    protected void writeAuthorization (String header){
        http.addRequestProperty("Authorization", header);
    }

    protected boolean httpOK() throws IOException{
       http.connect();
        var code = http.getResponseCode();
        if (code == HttpURLConnection.HTTP_OK){
            return true;
        }
        return false;
    }

    protected Object getResponse(Class<?> type) throws ConnectionException, IOException{
        var code = http.getResponseCode();
        if (code != HttpURLConnection.HTTP_OK){
            if (code == HttpURLConnection.HTTP_UNAUTHORIZED){
                Result result = new Result();
                result.authError();
                return result;
            }
            else if(code == HttpURLConnection.HTTP_FORBIDDEN){
                Result result = new Result();
                result.takenError();
                return result;
            }
            else{
                throw new ConnectionException(CONNECTION_ERROR_MSG);
                //error codes 404 and 500 (and 400 and that's on you)
            }
        }
        else{
            //return response body
            Object response = null;
            try(InputStream body = http.getInputStream()){
                InputStreamReader reader = new InputStreamReader(body);
                GsonBuilder builder = new GsonBuilder();
                builder.registerTypeAdapter(ChessPiece.class, new model.Deserializer());
                response = builder.create().fromJson(reader, type);
            }
            return response;
        }
    }

    

    
}
