package unitTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dataAccess.Database;
import dataAccess.GameDAO;
import model.UserGame;
import dataAccess.DataAccessException;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;

import  com.google.gson.Gson;

public class GameDAOTest {
    private GameDAO gdao = new GameDAO();
    private UserDAOTest udao = new UserDAOTest();
    private Database db  = new Database();
    private Connection conn;

    @BeforeEach
    public void setup() throws DataAccessException,SQLException{
        udao.setup();
        conn = db.getConnection();
        Gson gson = new Gson();
        String obj = gson.toJson(new UserGame(null).getGame());
        conn.prepareStatement("INSERT INTO chess.games (name, gameobj)"
        +" VALUES ('game', '"+obj+"'),('gamegame','"+obj+"'),"
        +"('Sabaac','"+obj+"')").executeUpdate();
    }

    @AfterEach
    public void clearDB()throws DataAccessException{
        gdao.clear();
        udao.clearDB();
        if (conn != null){
            db.closeConnection(conn);
        }
    }

    @Test
    @DisplayName("Positive Clear Test")
    public void clear() throws DataAccessException, SQLException{
        gdao.clear();
        String sql = "SELECT * FROM chess.games";
        ResultSet set = conn.prepareStatement(sql).executeQuery();
        Assertions.assertFalse(set.next());
    }

    @Test
    @DisplayName("Positive Get Games")
    public void pGet() throws DataAccessException, SQLException{
        HashSet<UserGame> games = new HashSet<>();
        String sql = "SELECT name, id FROM chess.games";
        ResultSet set = conn.prepareStatement(sql).executeQuery();
        while (set.next()){
            UserGame addme = new UserGame(set.getString("name"));
            addme.changeID(set.getInt("id"));
            games.add(addme);
        }
        Collection<UserGame> compare = gdao.getGames();
        Assertions.assertEquals(games, compare);
    }
    @Test
    @DisplayName("Negative Get Games") 
    public void nGet()throws DataAccessException, SQLException{
        gdao.clear();
        Collection<UserGame> compare = gdao.getGames();
        Assertions.assertTrue(compare.isEmpty());
    }

    @Test
    @DisplayName("Positive Insert Game")
    public void pInsert() throws DataAccessException, SQLException{
        gdao.insert(new UserGame("Dejarik"));
        String sql = "SELECT * FROM chess.games WHERE name = 'Dejarik'";
        ResultSet set = conn.prepareStatement(sql).executeQuery();
        Assertions.assertTrue(set.next());
        Assertions.assertEquals("Dejarik", set.getString("name"));        
        Assertions.assertEquals(new Gson().toJson(new UserGame("Dejarik").getGame()), set.getString("gameobj"));//check that there is a game

    }

    @Test
    @DisplayName("Negative Insert Game")
    public void nInsert(){
        Assertions.assertThrows(DataAccessException.class, ()-> gdao.insert(new UserGame(null)));
    }

    @Test
    @DisplayName("Positive FindName")
    public void pFindName() throws DataAccessException{
        UserGame game = gdao.find("gamegame");
        Assertions.assertEquals("gamegame", game.getGameName());
        Assertions.assertNotNull(game.getGame());
    }

    @Test
    @DisplayName("Negative FindName")
    public void nFindName(){
        Assertions.assertThrows(DataAccessException.class, ()->gdao.find("nan"),
        "not found");
    }

    @Test
    @DisplayName("Positive FindID")
    public void pFindID() throws DataAccessException, SQLException{
        String sql = "SELECT id FROM chess.games WHERE name = 'game'";
        ResultSet set = conn.prepareStatement(sql).executeQuery();
        Assertions.assertTrue(set.next());
        UserGame game = gdao.find(set.getInt("id"));
        Assertions.assertEquals("game", game.getGameName());
        Assertions.assertNotNull(game.getGame());
    }

    @Test
    @DisplayName("Negative FindID")
    public void nFindID(){
        Assertions.assertThrows(DataAccessException.class, ()-> gdao.find(0),
        "not found");
    }

    @Test
    @DisplayName("Positive Update")
    public void pUpdate() throws DataAccessException, SQLException{
        UserGame game = new UserGame("game");
        String sql = "SELECT id FROM chess.games WHERE name = 'game'";
        ResultSet set = conn.prepareStatement(sql).executeQuery();
        Assertions.assertTrue(set.next());
        game.changeID(set.getInt("id"));
        game.setBlackUsername("username");
        gdao.updateGame(game);
        sql = "SELECT * FROM chess.games WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, set.getInt("id"));
        set = stmt.executeQuery();
        Assertions.assertTrue(set.next());
        Assertions.assertEquals("game", set.getString("name"));
        Assertions.assertEquals("username", set.getString("blackUsername"));
        Assertions.assertNull(set.getString("whiteUsername"));
    }

    @Test
    @DisplayName ("Negative Update")
    public void nUpdate() throws DataAccessException,SQLException{
        UserGame game = new UserGame("hello");
        gdao.updateGame(game);
        String sql = "SELECT * FROM chess.games WHERE name = 'hello'";
        ResultSet set = conn.prepareStatement(sql).executeQuery();
        Assertions.assertFalse(set.next());
    }
}
