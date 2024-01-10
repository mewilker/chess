package request;

/**will be created from a deserialized Register json string<p>
 * register requests with username, password and email
 */
public class RegisterRequest {
    private String username;
    private String password;
    private String email;

    public RegisterRequest(){
        super();
    }

    public RegisterRequest(String name, String passwd, String mail){
        username = name;
        password = passwd;
        email = mail;
    }
    
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public String getUsername() {
        return username;
    }

    /**
     * 
     * @return true if request is valid
     */
    public boolean valid(){
        //TODO add empty string cases
        //TODO check email validity
        if(username==null || password==null || email==null){
            return false;
        }
        return true;
    }
}
