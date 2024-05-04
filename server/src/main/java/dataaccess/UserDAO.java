package dataaccess;


import java.util.HashSet;
import java.util.Collection;
import model.User;

import java.sql.*;

/**User Data Access Object
 * <p> manages users within the database
 */
public class UserDAO {


    //private static Collection<User> users = new HashSet<>();
    private final DatabaseManager db;

    public UserDAO() throws DataAccessException{
        db = new DatabaseManager();
    }

    public Collection<User> getUsers() throws DataAccessException{
        Collection<User> result = new HashSet<>();
        Connection conn = db.getConnection();
        String sql = "SELECT * FROM users";
        try{
            ResultSet set = conn.prepareStatement(sql).executeQuery();
            while(set.next()){
                result.add(new User(set.getString("username"), 
                    set.getString("password"), 
                    set.getString("email")));
            }
        }
        catch(SQLException e){
            throw new DataAccessException(e.getMessage());
        }
        return result;
    }
    
    /**Removes all users from the database */
    public void clear() throws DataAccessException{
        Connection conn = db.getConnection();
        String sql = "DELETE FROM users";
        try{
            conn.prepareStatement(sql).executeUpdate();
        }
        catch(SQLException e){
            throw new DataAccessException(e.getMessage());
        }       
        //legacy static set code 
        //users.clear();
    }
    
    /**
     * Inserts a User object in the database<p>
     * 
     * @param user
     * @throws DataAccessException if the user already exsists
     */
    public void insert(User user) throws DataAccessException{
        Connection conn = db.getConnection();
        try{
            find(user.getUserName());

        }
        catch (DataAccessException e){
            if (e.getMessage() == "not found"){
                try{
                    conn.setAutoCommit(false);
                    String sql = "INSERT INTO users (username, password, email) VALUES (?,?,?)";
                    PreparedStatement statement = conn.prepareStatement(sql);
                    statement.setString(1, user.getUserName());
                    statement.setString(2, user.getPassword());
                    statement.setString(3, user.getEmail());
                    statement.executeUpdate();
                    conn.commit();
                }
                catch(SQLException err){
                    db.rollback(conn);
                    db.closeConnection(conn);
                    throw new DataAccessException(err.getMessage());
                }
                db.closeConnection(conn);
                return;
            }
        }
        db.closeConnection(conn);
        throw new DataAccessException("already taken");
    }

    /**
     * 
     * @param username
     * @return User with username
     * @throws DataAccessException if it could not find it
     */
    public User find(String username) throws DataAccessException{
        Connection conn = db.getConnection();
        String sql = "SELECT * FROM users WHERE username = ?";
        User user;
        try{
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet set = statement.executeQuery();
            if(!set.next()){
                //System.out.println("the username " + username + " was not found");
                db.closeConnection(conn);
                throw new DataAccessException("not found");
            }

            user = new User(username, set.getString("password"), 
                set.getString("email"));
                
        }
        catch(SQLException e){
            db.closeConnection(conn);
            throw new DataAccessException(e.getMessage());
        }
        db.closeConnection(conn);
        return user;
    }

}
