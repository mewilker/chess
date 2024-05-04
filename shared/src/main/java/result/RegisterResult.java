package result;

/**A positive Register will always log the User in */
public class RegisterResult extends LoginResult{
    /**no arg constructor */
    public RegisterResult(){
        super();
    }

    /**
     * username and Authentication Token constructor
     * 
     * @param name username
     * @param token authToken
     */
    public RegisterResult(String name, String token){
        super(name, token);
    }

}
