package request;

/**authorizations headers object*/
public class AuthorizedRequest {
    private String authorization;

    public AuthorizedRequest(){
        super();
    }

    public AuthorizedRequest(String authToken){
        authorization = authToken;
    }
    
    public String getAuthToken(){
        return authorization;
    }
}
