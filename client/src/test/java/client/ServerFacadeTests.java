package client;

import org.junit.jupiter.api.*;
import server.Server;

import java.net.URISyntaxException;
import java.util.Collection;
import java.io.IOException;

import model.AuthToken;
import model.UserGame;
import serverFacade.*;


public class ServerFacadeTests {
    private ServerFacade facade;

    private static int port;

    private static Server server;

    @BeforeAll
    public static void init() {
        server = new Server();
        port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
    }

    @BeforeEach
    public void createFacade() throws URISyntaxException, IOException{
        facade = new ServerFacade("http://localhost:" + port);
        facade.cleardb();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    @DisplayName("Positive Register")
    public void goodRegister() throws URISyntaxException, ConnectionException{
        AuthToken token = facade.register("Artoo", "anakin", "r2d2@jeditemple.com");
        Assertions.assertNotNull(token.getAuthToken());
    }

    @Test
    @DisplayName("Negative Register")
    public void badRegister() throws URISyntaxException, ConnectionException{
        goodRegister();
        AuthToken token = facade.register("Artoo", "r2", "r2");
        Assertions.assertNull(token);
    }

    @Test
    @DisplayName("Positive Login")
    public void goodLogin()throws URISyntaxException, ConnectionException{
        goodRegister();
        AuthToken token = facade.login("Artoo", "anakin");
        Assertions.assertNotNull(token);
    }

    @Test
    @DisplayName("Negative Login")
    public void badLogin()throws URISyntaxException, ConnectionException{
        goodRegister();
        AuthToken token = facade.login("Artoo", "skywalker");
        Assertions.assertNull(token);
    }

    @Test
    @DisplayName("Positive Logout")
    public void goodLogout()throws URISyntaxException, ConnectionException{
        AuthToken token = facade.register("Artoo", "anakin", "r2d2@jeditemple.com");
        Assertions.assertDoesNotThrow(()->facade.logout(token.getAuthToken()));
    }

    @Test
    @DisplayName("Negative Logout")
    public void badLogout(){
        Assertions.assertThrows(ConnectionException.class,()->facade.logout("aaa"));
    }

    @Test
    @DisplayName("Positive Create Game")
    public void goodCreate() throws URISyntaxException, ConnectionException{
        AuthToken token = facade.register("Artoo", "anakin", "r2d2@jeditemple.com");
        int id = facade.createGame("sabaac", token.getAuthToken());
        Assertions.assertTrue(id > 0);
    }

    @Test
    @DisplayName("Negative Create Game")
    public void badCreate(){
        Assertions.assertThrows(ConnectionException.class,()->facade.createGame("sabaac", "aaa"));
    }

    @Test
    @DisplayName ("Positive List Games")
    public void goodList() throws URISyntaxException, ConnectionException{
        AuthToken token = facade.register("Artoo", "anakin", "r2d2@jeditemple.com");
        facade.createGame("sabaac", token.getAuthToken());
        Collection<UserGame> list = facade.list(token.getAuthToken());
        Assertions.assertNotNull(list);
    }

    @Test
    @DisplayName("Negative List Game")
    public void badList(){
        Assertions.assertThrows(ConnectionException.class,()->facade.list("aaa"));
    }

    @Test
    @DisplayName("Positive Join Game")
    public void goodJoin() throws URISyntaxException, ConnectionException{
        AuthToken token = facade.register("Artoo", "anakin", "r2d2@jeditemple.com");
        int id = facade.createGame("sabaac", token.getAuthToken());
        Assertions.assertDoesNotThrow(()->facade.join(token.getAuthToken(), id, "BLACK"));
    }

    @Test
    @DisplayName("Negative Join Game")
    public void badJoin() throws URISyntaxException, ConnectionException{
        AuthToken token = facade.register("Artoo", "anakin", "r2d2@jeditemple.com");
        int id = facade.createGame("sabaac", token.getAuthToken());
        facade.join(token.getAuthToken(), id, "BLACK");
        token = facade.register("null", "null", "null");
        final AuthToken auth = token;
        Assertions.assertThrows(ConnectionException.class, ()->facade.join(auth.getAuthToken(),id,"BLACK"));
    }

    @Test
    @DisplayName("Positive clear")
    public void clear() throws URISyntaxException, ConnectionException, IOException{
        goodRegister();
        facade.cleardb();
        AuthToken token = facade.register("Artoo", "anakin", "r2d2@jeditemple.com");
        Assertions.assertNotNull(token);
    }

    @Test
    @DisplayName ("Negative clear (bad connection)")
    public void doNotConnect() throws URISyntaxException{
        facade = new ServerFacade("null");
        Assertions.assertThrows(IllegalArgumentException.class, ()->facade.cleardb());
    }

}
