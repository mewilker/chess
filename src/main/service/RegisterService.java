package service;

import model.User;
import dataAccess.AuthDAO;
import dataAccess.UserDAO;
import dataAccess.DataAccessException;

import request.RegisterRequest;
import result.RegisterResult;

/**registers a new user */
public class RegisterService {
    
    AuthDAO adao = new AuthDAO();
    UserDAO udao = new UserDAO();

    
    /**
     * 
     * Registers a new user with the request, then logs them in
     * 
     * @param request username, email and password for registration
     * @return <ul> <li>username and authToken (Success) <li>bad request error <li>already taken error <li>general error</ul>
     */
    public RegisterResult register(RegisterRequest request){
        
        RegisterResult result = new RegisterResult();

        if (request == null || !request.valid()){
            //if request bad return error
            result.requestError();
            return result;
        }

        
        try{
            User user = new User(request.getUsername(),request.getPassword(),request.getEmail());
            udao.insert(user);
            result.setAuthToken(adao.genAuthToken(user.getUserName()));
            result.setUsername(user.getUserName());
        }
        catch (DataAccessException e){
            //if username taken return already taken error
            //other exceptions are general errors
            result.errorMessage(e.getMessage());
        }
        //return username and authtoken response
        return result;
    }
}
