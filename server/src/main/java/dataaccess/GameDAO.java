package dataaccess;

import java.util.HashSet;
import java.util.Collection;
import model.UserGame;

import java.sql.*;

import com.google.gson.*;

import chess.*;


/**Game Data Access Object
 * <p> manages games within the database
 */
public class GameDAO {
    
    //private static Collection<UserGame> games = new HashSet<>();
    private final DatabaseManager db;

    public GameDAO() throws DataAccessException{
        db = new DatabaseManager();
    }
    
    public Collection<UserGame> getGames() throws DataAccessException{
        HashSet<UserGame> result = new HashSet<>();
        Connection conn = db.getConnection();
        String sql = "SELECT * FROM games";
        try {
            ResultSet set = conn.prepareStatement(sql).executeQuery();
            while(set.next()){
                UserGame addme = parseSet(set);
                result.add(addme);
            }
        }
        catch(SQLException e){
            db.closeConnection(conn);
            throw new DataAccessException(e.getMessage());
        }
        db.closeConnection(conn);
        return result;
    }

    /**Removes all games from the database */
    public void clear() throws DataAccessException{
        Connection conn = db. getConnection();
        String sql = "DELETE FROM games";
        try{
            conn.prepareStatement(sql).executeUpdate();
        }
        catch(SQLException e){
            db.closeConnection(conn);
            throw new DataAccessException(e.getMessage());
        }
        db.closeConnection(conn);
        //games.clear();
    }

    /**
     * puts given Game in the database
     * 
     * @param userGame
     * @throws DataAccessException when gamename is already taken
     * @returns game id
     */
    public int insert(UserGame userGame) throws DataAccessException{
        if (userGame.getGameName() == null){
            throw new DataAccessException("no game name");
        }
        Connection conn = db.getConnection();
        ResultSet set = null;
        int result = 0;
        try{
            find(userGame.getGameName());
            String sql = "SELECT * FROM games WHERE name = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, userGame.getGameName());
            set = statement.executeQuery();
            int duplicates = 0;
            while (set.next()){
                duplicates++;
            }
            add(userGame, conn);
            sql = "SELECT id FROM games WHERE name = ?";
            statement = conn.prepareStatement(sql);
            statement.setString(1, userGame.getGameName());
            set = statement.executeQuery();
            while(duplicates > 0){
                if (!set.next()){
                    throw new DataAccessException("game was not inserted");
                }
                duplicates--;
            }
            conn.commit();
            set.next();
            result = set.getInt("id");
            db.closeConnection(conn);
            return result;
        }
        catch(DataAccessException e){
            if (e.getMessage().equals("not found")){
                try{
                    add(userGame, conn);
                    String sql = "SELECT id FROM games WHERE name = ?";
                    PreparedStatement statement = conn.prepareStatement(sql);
                    statement.setString(1, userGame.getGameName());
                    set = statement.executeQuery();
                    if (!set.next()){
                        throw new DataAccessException("game was not inserted");
                    }
                    conn.commit();
                    result = set.getInt("id");
                }
                catch (SQLException err){
                    db.rollback(conn);
                    db.closeConnection(conn);
                    throw new DataAccessException(err.getMessage());
                }
                db.closeConnection(conn);
                return result;
            }
        }
        catch (SQLException e){
            db.rollback(conn);
            db.closeConnection(conn);
            throw new DataAccessException(e.getMessage());
        }
        return result;
        
    }

    /**
     * finds game in database
     * 
     * @param gameID
     * @return Usergame object with matching game ID
     * @throws DataAccessException if not found
     */
    public UserGame find(int gameID) throws DataAccessException{
        Connection conn = db.getConnection();
        String sql = "SELECT * FROM games WHERE id = ?";
        UserGame game;
        try{
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, gameID);
            ResultSet set = statement.executeQuery();
            if (!set.next()){
                db.closeConnection(conn);
                throw new DataAccessException("not found");
            }
            game = parseSet(set);
        }
        catch (SQLException e){
            db.closeConnection(conn);
            throw new DataAccessException(e.getMessage());
        }
        db.closeConnection(conn);
        return game;
    }

    /**
     * finds game in database
     * 
     * @param gameName
     * @return Usergame object with matching game name
     * @throws DataAccessException if not found
     */
    public UserGame find (String gameName) throws DataAccessException{
        Connection conn = db.getConnection();
        String sql = "SELECT * FROM games WHERE name = ?";
        UserGame game;
        try{
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, gameName);
            ResultSet set = statement.executeQuery();
            if (!set.next()){
                db.closeConnection(conn);
                throw new DataAccessException("not found");
            }
            game = parseSet(set);
        }
        catch (SQLException e){
            db.closeConnection(conn);
            throw new DataAccessException(e.getMessage());
        }
        db.closeConnection(conn);
        return game;
    }

    public void updateGame(UserGame game) throws DataAccessException{
        Connection conn = db.getConnection();
        Gson gson = new Gson();
        String sql = "UPDATE games SET whiteUsername = ?, blackUsername = ?, "
            +"gameobj = ? WHERE id = ?";
        try{
            conn.setAutoCommit(false);
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, game.getWhiteUsername());
            statement.setString(2, game.getBlackUsername());
            statement.setString(3, gson.toJson(game.getGame()));
            statement.setInt(4, game.getGameID());
            statement.executeUpdate();
            conn.commit();
        }
        catch (SQLException e){
            db.rollback(conn);
            db.closeConnection(conn);
            throw new DataAccessException(e.getMessage());
        }
        db.closeConnection(conn);
    }

    private UserGame parseSet(ResultSet set) throws SQLException{
        UserGame game = new UserGame(set.getString("name"));
        game.changeID(set.getInt("id"));
        String userName = set.getString("blackUsername");
        if (userName!= null){
            game.setBlackUsername(userName);
        }
        userName = set.getString("whiteUsername");
        if (userName!= null){
            game.setWhiteUsername(userName);
        }
        //game serialization stuff
        String json = set.getString("gameobj");
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ChessPiece.class, new model.Deserializer());;
        game.updateGame(builder.create().fromJson(json, ChessGame.class));
        return game;
    }

    private void add(UserGame game, Connection conn) throws DataAccessException{
        Gson gson = new Gson();
        try{
            conn.setAutoCommit(false);
            String sql = "INSERT INTO games (name, gameobj) VALUES (?,?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, game.getGameName());
            statement.setString(2, gson.toJson(game.getGame()));
            statement.executeUpdate();
        }
        catch (SQLException err){
            db.rollback(conn);
            db.closeConnection(conn);
            throw new DataAccessException(err.getMessage());
        }
    }


}

