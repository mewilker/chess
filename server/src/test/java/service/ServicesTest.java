package service;

import java.util.Collection;

import org.junit.jupiter.api.*;

import dataaccess.DataAccessException;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.*;
import request.*;
import result.*;
import service.*;

import org.mindrot.jbcrypt.BCrypt;

public class ServicesTest {
    
    private static AuthDAO authdao;
    private static UserDAO userdao;
    private static GameDAO gamedao;

    Collection<User> users;
    Collection<AuthToken> tokens;
    Collection<UserGame> games;

    private String token;
    private UserGame game;

    @BeforeAll
    public static void dbInit() throws DataAccessException{
        authdao = new AuthDAO();
        userdao = new UserDAO();
        gamedao = new GameDAO();
    }
    
    @BeforeEach
    public void setup() throws DataAccessException{
        gamedao.clear();
        authdao.clear();
        userdao.clear();
        userdao.insert(new User("username", "password", "email"));
        token = authdao.genAuthToken("username");
        userdao.insert(new User("me", "password", "email"));        
        authdao.genAuthToken("me");
        userdao.insert(new User("C3PO", BCrypt.hashpw("droid", BCrypt.gensalt()),"Threepio@jeditemple.com"));
        gamedao.insert(new UserGame("gamegame"));
        int id = gamedao.insert(new UserGame("game"));
        game = new UserGame("Sabaac");
        game.changeID(id);
        gamedao.insert(game);

        users = userdao.getUsers();
        tokens = authdao.getTokens();
        games = gamedao.getGames();
    }

    @Test
    @DisplayName("Positive Clear Test")
    public void clearTest(){
        ClearApp service = new ClearApp();
        Result result = service.clearApp();
        Assertions.assertNull(result.getMessage()); // this may need to change, getting 404 errors
    }

    @Test
    @DisplayName("Register")
    public void registerNew() throws DataAccessException{
        RegisterService service = new RegisterService();
        RegisterResult result = service.register(new RegisterRequest("R2-D2","skywalkers","Artoo@jeditemple.com"));
        Assertions.assertNotNull(result.getAuthToken());
        Assertions.assertEquals("R2-D2", result.getUsername());
        tokens.add(new AuthToken("R2-D2", result.getAuthToken()));
        users.add(new User("R2-D2", "skywalkers", "Artoo@jeditemple.com"));
        Assertions.assertEquals(tokens, authdao.getTokens());
        Assertions.assertEquals(users.size(), userdao.getUsers().size());
    }

    @Test
    @DisplayName("Repeat Register")
    public void registerRepeat(){
        RegisterService service = new RegisterService();
        RegisterResult result = service.register(new RegisterRequest("username","password","email"));
        RegisterResult message = new RegisterResult();
        message.takenError();
        Assertions.assertEquals(message.getMessage(), result.getMessage());
    }


    @Test
    @DisplayName ("Wrong Password")
    public void invalidPassword(){
        LoginService service = new LoginService();
        LoginResult result = service.login(new LoginRequest("C3PO", "skywalkers"));
        LoginResult message = new LoginResult();
        message.authError();
        Assertions.assertEquals(message.getMessage(), result.getMessage());
    }

    @Test
    @DisplayName("Logout with dead AuthToken")
    public void badlogout(){
        LogoutService service = new LogoutService();
        Result result = service.logout(new AuthorizedRequest("aaa"));
        Result message = new Result();
        message.authError();
        Assertions.assertEquals(message.getMessage(), result.getMessage());
    }

    @Test
    @DisplayName("Unauthorized List Games")
    public void unauthorizedGames(){
        ListGamesService service = new ListGamesService();
        ListGamesResult result = service.listGames(new AuthorizedRequest("aaa"));
        ListGamesResult message = new ListGamesResult();
        message.authError();
        Assertions.assertEquals(message.getMessage(), result.getMessage());
    }

    @Test
    @DisplayName("Unauthorized creategame")
    public void cantMakeGame(){
        CreateGameService service = new CreateGameService();
        CreateGameResult result = service.createGame(new CreateGameRequest("aaa", "Dejarik"));
        CreateGameResult message = new CreateGameResult();
        message.authError();
        Assertions.assertEquals(message.getMessage(), result.getMessage());
    }

    @Test
    @DisplayName("No spots open")
    public void gamefull() throws DataAccessException{
        game.setBlackUsername("C3PO");
        gamedao.updateGame(game);
        JoinGameService service = new JoinGameService();
        Result result = service.joinGame(new JoinGameRequest(token, game.getGameID(),"BLACK"));
        Result message = new Result();
        message.takenError();
        Assertions.assertEquals(message.getMessage(), result.getMessage());
    }

    @Test
    @DisplayName("Unauthorized Join")
    void joinloggedout(){
        JoinGameService service = new JoinGameService();
        Result result = service.joinGame(new JoinGameRequest("aaa", game.getGameID(),"BLACK"));
        Result message = new Result();
        message.authError();
        Assertions.assertEquals(message.getMessage(), result.getMessage());
    }
}
