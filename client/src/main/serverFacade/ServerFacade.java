package serverFacade;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;

import model.AuthToken;
import model.UserGame;
import request.*;
import result.*;

public class ServerFacade extends HttpCommunicator{
    String baseURL;
    
    public ServerFacade (String url) throws URISyntaxException{
        super(url);
        baseURL = url;
    }

    public AuthToken register(String username, String password, String email) throws ConnectionException{
        AuthToken token = null;
        try{
            endpoint(baseURL + "/user");
            initHTTP("POST");
            RegisterRequest body = new RegisterRequest(username, password, email);
            writeBody(body);
            if (httpOK()){
                RegisterResult result = (RegisterResult) getResponse(RegisterResult.class);
                token = new AuthToken(username, result.getAuthToken());
            }
            else {
                Result result = (Result) getResponse(Result.class);
                //message should be already taken
            }
        }
        catch(IOException e){
            throw new ConnectionException(CONNECTION_ERROR_MSG);
        }
        catch(URISyntaxException e){
            throw new ConnectionException(CONNECTION_ERROR_MSG);
        }
        return token;
    }

    public void logout(String authToken)throws ConnectionException{
        try {
            endpoint(baseURL+"/session");
            initHTTP("DELETE");
            writeAuthorization(authToken);
            if (httpOK()){
                return;
            }
            else {
                throw new ConnectionException("Session has already ended.\n");
            }
        } catch (IOException e) {
            throw new ConnectionException(CONNECTION_ERROR_MSG);
        }
        catch(URISyntaxException e){
            throw new ConnectionException(CONNECTION_ERROR_MSG);
        }
    }

    public AuthToken login(String username, String password) throws ConnectionException{
        AuthToken token = null;
        try{
            endpoint(baseURL+"/session");
            initHTTP("POST");
            LoginRequest body = new LoginRequest(username, password);
            writeBody(body);
            if (httpOK()){
                LoginResult result = (LoginResult) getResponse(LoginResult.class);
                token = new AuthToken(username, result.getAuthToken());
            }
            else {
                Result result = (Result) getResponse(Result.class);
                //message should be unathorized
            }
        }
        catch(IOException e){
            throw new ConnectionException(CONNECTION_ERROR_MSG);
        }
        catch(URISyntaxException e){
            throw new ConnectionException(CONNECTION_ERROR_MSG);
        }
        return token;
    }

    public Collection<UserGame> list(String token) throws ConnectionException{
        Collection<UserGame> games = null;
        try{
            endpoint(baseURL+"/game");
            initHTTP("GET");
            writeAuthorization(token);
            if (httpOK()){
                ListGamesResult result = (ListGamesResult) getResponse(ListGamesResult.class);
                games = result.getGames();
            }
            else{
                throw new ConnectionException(CONNECTION_ERROR_MSG);
            }
        }
        catch (IOException e){
            throw new ConnectionException(CONNECTION_ERROR_MSG);
        }
        catch(URISyntaxException e){
            throw new ConnectionException(CONNECTION_ERROR_MSG);
        }
        return games;
    }

    public void join(String token, int id, String color) throws ConnectionException{
        try{
            endpoint(baseURL+"/game");
            initHTTP("PUT");
            writeAuthorization(token);
            JoinGameRequest body = new JoinGameRequest(token, id, color);
            writeBody(body);
            if (httpOK()){
                //TODO WEBSOCKET
            }
            else{
                Result result = (Result) getResponse(Result.class);
                Result message = new Result();
                //valid id checking
                message.authError();
                if (result.getMessage().equals(message.getMessage())){
                    throw new ConnectionException("ID was invalid. Are you sure the game exists?\n");
                }
                message.takenError();
                if (result.getMessage().equals(message.getMessage())){
                    throw new ConnectionException("That team has been taken for the specified game.\n");
                }
                //spot taken checking
            }
        }
        catch(IOException e){
            throw new ConnectionException(CONNECTION_ERROR_MSG);
        }
        catch(URISyntaxException e){
            throw new ConnectionException(CONNECTION_ERROR_MSG);
        }
    }

    public int createGame(String name, String authToken) throws ConnectionException{
        int id = 0;
        try{
            endpoint(baseURL+"/game");
            initHTTP("POST");
            writeAuthorization(authToken);
            CreateGameRequest body = new CreateGameRequest(authToken, name);
            writeBody(body);
            if (httpOK()){
                CreateGameResult result = (CreateGameResult) getResponse(CreateGameResult.class);
                id = result.getGameID();
            }
            else{
                throw new ConnectionException(CONNECTION_ERROR_MSG);
            }
        }
        catch(IOException e){
            throw new ConnectionException(CONNECTION_ERROR_MSG);
        }
        catch(URISyntaxException e){
            throw new ConnectionException(CONNECTION_ERROR_MSG);
        }
        return id;
    }

    /**
     * For testing purposes only :)
     * clears all data from ta database
     * 
     * @throws IOException
     * @throws URISyntaxException
     */
    public void cleardb() throws IOException, URISyntaxException{
        endpoint(baseURL+"/db");
        initHTTP("DELETE");
        httpOK();
    }
}
