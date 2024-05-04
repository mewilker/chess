package request;

/**login requests with username and password */
public class LoginRequest {
    private String username;
    private String password;

    public LoginRequest(){
        super();
    }

    public LoginRequest(String name, String passwd){
        username = name;
        password = passwd;
    }
    
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    /**
     * 
     * @return true if all fields have something
     */
    public boolean valid(){
        return username != null && password != null && !username.isEmpty() && !password.isEmpty();
    }
}
