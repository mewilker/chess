package result;

/**holds either the users new auth Token or the error message*/
public class LoginResult extends Result{
    String username;
    String authToken;

    /**no arg constructor */
    public LoginResult (){
        super();
    }

    /**
     * Creates a Login result 
     * 
     * @param name - username
     * @param token - authToken
     */
    public LoginResult (String name, String token){
        username = name;
        authToken = token;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getUsername() {
        return username;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
