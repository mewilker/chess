package model;

/**user has a username, password and email */
public class User {
    private String userName;
    private String password;
    private String email;

    public User (){
        super();
    }

    /**
     * creates a User with a username, email and password
     * 
     * @param name
     * @param inPasswd
     * @param inEmail
     */
    public User(String name, String inPasswd, String inEmail){
        userName = name;
        password = inPasswd;
        email = inEmail;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    /**
     * authenticates password
     * 
     * @param passwd
     * @return true if password is correct
     */
    public boolean authenticate(String passwd){
        if (passwd.equals(password)){
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((userName == null) ? 0 : userName.length());
        result = prime * result + ((password == null) ? 0 : password.length());
        result = prime * result + ((email == null) ? 0 : email.length());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (userName == null) {
            if (other.userName != null)
                return false;
        } else if (!userName.equals(other.userName))
            return false;
        if (password == null) {
            if (other.password != null)
                return false;
        } else if (!password.equals(other.password))
            return false;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
            return false;
        return true;
    }

}
