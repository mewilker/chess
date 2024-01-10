package service;

import dataAccess.DataAccessException;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import request.AuthorizedRequest;
import result.ListGamesResult;

/**gives a list of all games */
public class ListGamesService {
    
    AuthDAO adao = new AuthDAO();
    GameDAO gdao = new GameDAO();
    
    /**
     * Gives a list of all games from an auth token, returns list result
     * 
     * @param request - authToken
     * @return <ul> <li>the list of Games if successful <li>authentication error <li>general error </ul>
     */
    public ListGamesResult listGames(AuthorizedRequest request){
        ListGamesResult result = new ListGamesResult();
        //Check if authToken in database
        try{
            adao.findToken(request.getAuthToken());
            result = new ListGamesResult(gdao.getGames());
        }
        catch(DataAccessException e){
            if(e.getMessage().equals("not found")){
                result.authError();
            }
            //other exceptions
            else{
                result.errorMessage(e.getMessage());
            }
        }

        return result;
    }
}
