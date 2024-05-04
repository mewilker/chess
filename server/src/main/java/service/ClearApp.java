package service;

import result.Result;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;

/**Clear Application clears the entire database */
public class ClearApp {
    
    AuthDAO adao;
    GameDAO gdao;
    UserDAO udao;
    
    /**
     * Removes all Users, UserGames and AuthTokens
     * 
     * @return null if success, error message if failed
     */

    public Result clearApp(){
        try {
            gdao = new GameDAO();
            adao = new AuthDAO();
            udao = new UserDAO();

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
