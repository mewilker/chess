package dataaccess;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dataaccess.UserDAO;
import model.User;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;

public class UserDAOTest {
    private static UserDAO udao;
    private static DatabaseManager db;
    private Connection conn;

    @BeforeAll
    public static void cleanMeOut() throws DataAccessException, SQLException{
        Connection connect = db.getConnection();
        String sql = "DELETE FROM authtokens;";
        connect.prepareStatement(sql).executeUpdate();
        sql = "DELETE FROM games";
        connect.prepareStatement(sql).executeUpdate();
        db.closeConnection(connect);
    }

    @BeforeEach
    public void setup() throws SQLException,DataAccessException{
        udao = new UserDAO();
        db = new DatabaseManager();
        new AuthDAO().clear();
        udao.clear();
        conn = db.getConnection();
        conn.prepareStatement("INSERT INTO users (username, password, email)"+
            " VALUES ('username', 'password', 'email'),('me', 'password','email'),('C3PO', "+
            "'droid','Threepio@jeditemple.com');").executeUpdate();
        
    }

    @AfterEach
    public void clearDB()throws DataAccessException{
        if (conn != null){
            db.closeConnection(conn);
        }
    }

    @Test
    @DisplayName("Positive Clear Test")
    public void clear()throws SQLException, DataAccessException{
        udao.clear();
        String sql = "SELECT * FROM users";
        ResultSet set = conn.prepareStatement(sql).executeQuery();
        Assertions.assertFalse(set.next());
    }

    @Test
    @DisplayName("Positive Get Users")
    public void pGet() throws DataAccessException{
        HashSet<User> users = new HashSet<>();
        users.add(new User("username", "password", "email"));
        users.add(new User("me", "password", "email"));
        users.add(new User("C3PO", "droid","Threepio@jeditemple.com"));
        Collection<User> compare = udao.getUsers();
        Assertions.assertEquals(users, compare);
    }

    @Test
    @DisplayName("Negative Get Users")
    public void nGet() throws DataAccessException{
        udao.clear();
        Collection<User> compare = udao.getUsers();
        Assertions.assertTrue(compare.isEmpty());
    }

    @Test
    @DisplayName("Positive Insert User")
    public void pInsert() throws DataAccessException, SQLException{
        User artoo = new User("R2-D2","skywalkers","Artoo@jeditemple.com");
        udao.insert(artoo);
        String sql = "SELECT * FROM users WHERE username = 'R2-D2'";
        ResultSet set = conn.prepareStatement(sql).executeQuery();
        Assertions.assertTrue(set.next());
        Assertions.assertEquals(artoo.getUserName(), set.getString("username"));
        Assertions.assertEquals(artoo.getPassword(), set.getString("password"));
        Assertions.assertEquals(artoo.getEmail(), set.getString("email"));
    }

    @Test
    @DisplayName("Negative Insert User")
    public void nInsert(){
        User threepio = new User("C3PO", "password", "email");
        Assertions.assertThrows(DataAccessException.class, ()->udao.insert(threepio),
         "already taken");
    }

    @Test
    @DisplayName("Positive Find Test")
    public void pFind() throws DataAccessException, SQLException{
        User threepio = udao.find("C3PO");
        String sql = "SELECT * FROM users WHERE username = 'C3PO'";
        ResultSet set = conn.prepareStatement(sql).executeQuery();
        Assertions.assertTrue(set.next());
        Assertions.assertEquals(threepio.getUserName(), set.getString("username"));
        Assertions.assertEquals(threepio.getPassword(), set.getString("password"));
        Assertions.assertEquals(threepio.getEmail(), set.getString("email"));
    }

    @Test
    @DisplayName ("Negative find test")
    public void nFind(){
        Assertions.assertThrows(DataAccessException.class, ()->udao.find("R2-D2"),
         "not found");
    }
}
