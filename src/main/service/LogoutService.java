package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import request.AuthorizedRequest;
import result.Result;

/**logs out a user */
public class LogoutService {
    
    AuthDAO adao = new AuthDAO();
    
    /**
     * Logs out the user represented by the authToken
     * 
     * @param request - authToken
     * @return <ul> <li>null if successful <li>unauthorized error <li>general error </ul>
     */
    public Result logout(AuthorizedRequest request){
        Result result = new Result();
        try{
            adao.remove(request.getAuthToken());
        }
        catch(DataAccessException e){
            //return unauthorized if authToken doesn't exist
            if (e.getMessage().equals("not found")){
                result.authError();
            }
            //general error
            else{
                result.errorMessage(e.getMessage());
            }
        }

        //return nothing if sucessful removing of auth token
        return result;
    }
}
