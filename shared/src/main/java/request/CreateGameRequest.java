package request;

/**create game requests with authorization header and name */
public class CreateGameRequest {
    private AuthorizedRequest authToken;
    private String gameName;

    public CreateGameRequest(){
        super();
    }

    public CreateGameRequest(String token, String name){
        authToken = new AuthorizedRequest(token);
        gameName = name;
    }
    
    public String getAuthToken() {
        return authToken.getAuthToken();
    }

    public String getGameName(){
        return gameName;
    }

    public void setAuthToken(String token){
        authToken = new AuthorizedRequest(token);
    }

    /**
     * 
     * @return true if request is valid
     */
    public boolean valid(){
        if (this.authToken == null || gameName ==null||gameName == ""){
            return false;
        }
        return true;
    }

}
