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
     * @param Token authToken
     */
    public RegisterResult(String name, String Token){
        super(name, Token);
    }

}
