package unitTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dataAccess.AuthDAO;
import model.AuthToken;
import dataAccess.DataAccessException;
import dataAccess.Database;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;

public class AuthDAOTest {
    private UserDAOTest udao = new UserDAOTest();
    private AuthDAO adao = new AuthDAO();
    private Database db = new Database();
    private Connection conn;

    @BeforeEach
    public void setup() throws DataAccessException, SQLException{
        udao.setup();
        conn = db.getConnection();
        conn.prepareStatement("INSERT INTO chess.authtokens (username, token)"+
            " VALUES ('username', 'abc'),('me', 'abd'),('C3PO', "+
            "'abe');").executeUpdate();
        
    }

    @AfterEach
    public void clearDB()throws DataAccessException{
        adao.clear();
        udao.clearDB();
        if (conn != null){
            db.closeConnection(conn);
        }
    }

    @Test
    @DisplayName("Positive Clear Test")
    public void clear() throws DataAccessException,SQLException{
        adao.clear();
        String sql = "SELECT * FROM chess.authtokens";
        ResultSet set = conn.prepareStatement(sql).executeQuery();
        Assertions.assertFalse(set.next());
    }

    @Test
    @DisplayName("Positive Get Tokens")
    public void pGet() throws DataAccessException{
        HashSet<AuthToken> tokens = new HashSet<>();
        tokens.add(new AuthToken("username", "abc"));
        tokens.add(new AuthToken("me", "abd"));
        tokens.add(new AuthToken("C3PO", "abe"));
        Collection<AuthToken> compare = adao.getTokens();
        Assertions.assertEquals(tokens, compare);
    }
    @Test
    @DisplayName("Negative Get Tokens") 
    public void nGet()throws DataAccessException{
        adao.clear();
        Collection<AuthToken> compare = adao.getTokens();
        Assertions.assertTrue(compare.isEmpty());
    }

    @Test
    @DisplayName("Positive Insert Token")
    public void pInsert() throws DataAccessException, SQLException{
        udao.pInsert();
        adao.genAuthToken("R2-D2");
        String sql = "SELECT * FROM chess.authtokens WHERE username = 'R2-D2'";
        ResultSet set = conn.prepareStatement(sql).executeQuery();
        Assertions.assertTrue(set.next());
        Assertions.assertEquals("R2-D2", set.getString("username"));
        Assertions.assertNotNull(set.getString("token"));
    }

    @Test
    @DisplayName("Negative Insert Token") //I literally have no idea how to test this, my gen token will always insert a token...
    public void nInsert() throws DataAccessException,SQLException{
        adao.genAuthToken("C3PO");
        String sql = "SELECT * FROM chess.authtokens WHERE username = 'C3PO'";
        ResultSet set = conn.prepareStatement(sql).executeQuery();
        Assertions.assertTrue(set.next());
        Assertions.assertEquals("C3PO", set.getString("username"));
        Assertions.assertEquals("abe", set.getString("token"));
        Assertions.assertTrue(set.next());
    }
    //these may need to change, may not be "unique tokens"

    @Test
    @DisplayName("Positive FindName")
    public void pFindName() throws DataAccessException, SQLException{
        AuthToken Threepio = adao.findName("C3PO");
        String sql = "SELECT * FROM chess.authtokens WHERE username = 'C3PO'";
        ResultSet set = conn.prepareStatement(sql).executeQuery();
        Assertions.assertTrue(set.next());
        Assertions.assertEquals(Threepio.getUserName(), set.getString("username"));
        Assertions.assertEquals(Threepio.getAuthToken(), set.getString("token"));
    }

    @Test
    @DisplayName("Negative FindName")
    public void nFindName(){
        Assertions.assertThrows(DataAccessException.class, ()->adao.findName("R2-D2"),
         "not found");
    }

    @Test
    @DisplayName("Positive FindToken")
    public void pFindToken() throws DataAccessException, SQLException{
        AuthToken Threepio = adao.findToken("abe");
        String sql = "SELECT * FROM chess.authtokens WHERE token = 'abe'";
        ResultSet set = conn.prepareStatement(sql).executeQuery();
        Assertions.assertTrue(set.next());
        Assertions.assertEquals(Threepio.getUserName(), set.getString("username"));
        Assertions.assertEquals(Threepio.getAuthToken(), set.getString("token"));
    }

    @Test
    @DisplayName("Negative FindToken")
    public void nFindToken(){
        Assertions.assertThrows(DataAccessException.class, ()->adao.findToken("R2-D2"),
         "not found");
    }

    @Test
    @DisplayName("Positive Remove")
    public void pRemove() throws DataAccessException, SQLException{
        adao.remove("abc");
        String sql = "SELECT * FROM chess.authtokens WHERE token = 'abc'";
        ResultSet set = conn.prepareStatement(sql).executeQuery();
        Assertions.assertFalse(set.next());
    }

    @Test
    @DisplayName("Negative Remove")
    public void nRemove(){
        Assertions.assertThrows(DataAccessException.class, ()->adao.remove("R2-D2"),
         "not found");
    }

    

}
