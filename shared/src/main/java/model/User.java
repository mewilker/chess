package model;

import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        User user = (User) o;
        return Objects.equals(userName, user.userName) && Objects.equals(password, user.password)
                && Objects.equals(email, user.email);
    }
}
