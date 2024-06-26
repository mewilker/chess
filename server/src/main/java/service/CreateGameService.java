package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.UserGame;
import request.CreateGameRequest;
import result.CreateGameResult;

/**Creates a new game from a game name */
public class CreateGameService {
    
    AuthDAO adao;
    GameDAO gdao;
    /**
     * Creates a new Game from a request with a result
     * 
     * @param request - contains game name
     * @return <ul> <li>game id if success <li>bad request error <li>unauthorized error <li>general error </ul>
     */
    public CreateGameResult createGame (CreateGameRequest request){
        CreateGameResult result = new CreateGameResult();
        try{
            adao = new AuthDAO();
            gdao = new GameDAO();
            //check request
            if(request == null || !request.valid()){
                result.requestError();
                return result;
            }
            
            //check authorizatrion
            adao.findToken(request.getAuthToken());
            //
            UserGame game = new UserGame(request.getGameName());
            game.changeID(gdao.insert(game));
            result = new CreateGameResult(game.getGameID());
        }
        catch(DataAccessException e){
            if (e.getMessage().equals("not found")){
                result.authError();
                return result;
            }
            //other exceptions
            else{
                result.errorMessage(e.getMessage());
            }
        }
        
        //return result
        return result;
    }
}
