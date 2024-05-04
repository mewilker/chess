package dataaccess;

import java.util.Collection;
import java.util.HashSet;
import model.AuthToken;

import java.sql.*;

/**Authentication Token Data Access Object
 * <p> manages tokens within the database
 */
public class AuthDAO {
    
    //private static Collection<AuthToken> tokens = new HashSet<>();
    private final DatabaseManager db;
    String lastGenerated = "abc";

    public AuthDAO() throws DataAccessException{
        db = new DatabaseManager();
    }

    public Collection<AuthToken> getTokens() throws DataAccessException{
        Collection<AuthToken> result = new HashSet<>();
        Connection conn = db.getConnection();
        String sql = "SELECT * FROM authtokens";
        try{
            ResultSet set = conn.prepareStatement(sql).executeQuery();
            while(set.next()){
                result.add(new AuthToken(set.getString("username"), 
                    set.getString("token")));
            }
        }
        catch(SQLException e){
            db.closeConnection(conn);
            throw new DataAccessException(e.getMessage());
        }
        db.closeConnection(conn);
        return result;
        
        /**Collection<AuthToken> copy = new HashSet<>();
        for (AuthToken token : tokens){
            copy.add(token);
        }
        return copy;*/
    }
    
    /**Removes all auth tokens from the database */
    public void clear() throws DataAccessException{
        Connection conn = db.getConnection();
        String sql = "DELETE FROM authtokens";
        try{
            conn.prepareStatement(sql).executeUpdate();
        }
        catch(SQLException e){
            db.closeConnection(conn);
            throw new DataAccessException(e.getMessage());
        }
        db.closeConnection(conn);
        lastGenerated = "abc";
    }

    /**
     * inserts authToken object into the database
     * 
     * @param authToken
     * @throws DataAccessException - if the authToken already exists
     */
    private void insert(AuthToken authToken) throws DataAccessException{
        if (authToken.getAuthToken() == null){
            throw new DataAccessException("tried to insert a blank auth token");
        }
        Connection conn = db.getConnection();
        try{
            findToken(authToken.getAuthToken());
            
        }
        catch(DataAccessException e){
            if (e.getMessage().equals("not found")){
                try{
                    conn.setAutoCommit(false);
                    String sql = "INSERT INTO authtokens (username, token) VALUES (?,?)";
                    PreparedStatement statement = conn.prepareStatement(sql);
                    statement.setString(1, authToken.getUserName());
                    statement.setString(2, authToken.getAuthToken());
                    statement.executeUpdate();
                    conn.commit();
                }
                catch(SQLException err){
                    db.rollback(conn);
                    db.closeConnection(conn);
                    throw new DataAccessException(err.getMessage());
                }
            }
            db.closeConnection(conn);
            return;
        }
        db.closeConnection(conn);
        throw new DataAccessException("already taken");
    }

    /**
     * finds authToken in the database
     * 
     * @param userName
     * @return Authtoken matching username
     * @throws DataAccessException - when not found
     */
    public AuthToken findName(String userName) throws DataAccessException{
        Connection conn = db.getConnection();
        String sql = "SELECT token FROM authtokens WHERE username = ?";
        AuthToken auth;
        try{
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, userName);
            ResultSet set = statement.executeQuery();
            if (!set.next()){
                db.closeConnection(conn);
                throw new DataAccessException("not found");
            }
            auth = new AuthToken(userName, set.getString("token"));    
        }
        catch (SQLException e){
            db.closeConnection(conn);
            throw new DataAccessException(e.getMessage());
        }
        db.closeConnection(conn);
        return auth;
    }

    /**
     * finds authToken in the database
     * 
     * @param authToken
     * @return Authoken matching token
     * @throws DataAccessException - when not found
     */
    public AuthToken findToken(String authToken) throws DataAccessException{
        Connection conn = db.getConnection();
        String sql = "SELECT username FROM authtokens WHERE token = ?";
        AuthToken auth;
        try{
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, authToken);
            ResultSet set = statement.executeQuery();
            if (!set.next()){
                db.closeConnection(conn);
                throw new DataAccessException("not found");
            }
            auth = new AuthToken(set.getString("username"), authToken);    
        }
        catch (SQLException e){
            db.closeConnection(conn);
            throw new DataAccessException(e.getMessage());
        }
        db.closeConnection(conn);
        return auth;
    }

    /**
     * removes authToken from database
     * 
     * @param authToken - String
     * @throws DataAccessException if authToken is not in the database
     */
    public void remove(String authToken) throws DataAccessException{
        findToken(authToken);
        Connection conn = db.getConnection();
        String sql = "DELETE FROM authtokens WHERE token = ?";
        try{
            conn.setAutoCommit(false);
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, authToken);
            statement.executeUpdate();
            conn.commit();
        }
        catch (SQLException e){
            db.rollback(conn);
            db.closeConnection(conn);
            throw new DataAccessException(e.getMessage());
        }
    }

    public String genAuthToken(String user) throws DataAccessException{
        AuthToken auth = new AuthToken(user, changeToken());
        insert(auth);
        return lastGenerated;
    }

    private String changeToken(){
        try{
            while(findToken(lastGenerated)!= null){
                char addme = lastGenerated.charAt(lastGenerated.length()-1);
                //System.out.println(addme);
                if (addme == 'z'){
                    lastGenerated = lastGenerated+"A";
                }
                else{
                    lastGenerated = lastGenerated.replace(addme, ++addme);
                }
            }
        }
        catch (DataAccessException e){
            return lastGenerated;
        }
        return null;
    }

}
