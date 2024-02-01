package service;

import result.Result;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;

/**Clear Application clears the entire database */
public class ClearApp {
    
    AuthDAO adao = new AuthDAO();
    GameDAO gdao = new GameDAO();
    UserDAO udao = new UserDAO();
    
    /**
     * Removes all Users, UserGames and AuthTokens
     * 
     * @return null if success, error message if failed
     */
    public Result clearApp(){
        try {
            gdao.clear();
            adao.clear();
            udao.clear();
        } 
        catch (DataAccessException e) {
            Result result = new Result();
            result.errorMessage(e.getMessage());
            return result;
        }
        return new Result();
    }
}
