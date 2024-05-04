package model;

/**stores an Authentication Token with a username */
public class AuthToken {

    private String authToken;
    private String userName;

    public AuthToken (String user, String token){
        userName = user;
        authToken = token;
    }
    
    public String getAuthToken() {
        return authToken;
    }
    public String getUserName() {
        return userName;
    }

    @Override
    public int hashCode(){
        int prime = 31;
        int result = 1;
        for(int i = 0; i < authToken.length(); i++){
            result += authToken.charAt(i)*prime;
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this==obj){
            return true;
        }
        if (obj==null){
            return false;
        }
        if (getClass() != obj.getClass()){
            return false;
        }
        AuthToken other = (AuthToken)obj;
        if (!userName.equals(other.userName)){
            return false;
        }
        if (!authToken.equals(other.authToken)){
            return false;
        }
        return true;
    }
    
}
