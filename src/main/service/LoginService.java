package service;

import dataAccess.DataAccessException;
import dataAccess.AuthDAO;
import dataAccess.UserDAO;
import model.User;

import request.LoginRequest;
import result.LoginResult;

/**logs in a user */
public class LoginService {
    
    AuthDAO adao = new AuthDAO();
    UserDAO udao = new UserDAO();

    /**
     * Logs in an exsisting user<p> Returns new auth Token in result
     * 
     * @param request for login
     * @return <ul> <li>successful result(auth token) <li>authorization error <li>general error</ul>
     */
    public LoginResult login (LoginRequest request){
        LoginResult result = new LoginResult();

        if (request == null || !request.valid()){
            result.requestError();
            return result;
        }

        try{
            User user = udao.find(request.getUsername());
            if (user.authenticate(request.getPassword())){
                //return username and authtoken response
                result.setAuthToken(adao.genAuthToken(user.getUserName()));
                result.setUsername(user.getUserName());
            }
            else {
            //if username and password no match return error
                result.authError();
            }
        }
        catch (DataAccessException e){
            //no username in users
            if (e.getMessage().equals("not found")){
                result.authError();
            }
            //other exceptions are general errors
            else{
                result.errorMessage(e.getMessage());
            }
        }


        return result;
    }
}
