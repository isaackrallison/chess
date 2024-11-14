package client;

import com.sun.source.tree.AssertTree;
//import model.RegisterRequest;
import exception.ResponseException;
import model.*;
import org.junit.jupiter.api.*;
import server.Server;
import ui.Repl;
import ui.ServerFacade;


public class ServerFacadeTests {

    private static Server new_server;
    static ServerFacade server;

    @BeforeAll
    public static void init() {
        new_server = new Server();
        var port = new_server.run(8080);
        server = new ServerFacade("http://localhost:" + port);
        System.out.println("Started test HTTP server on " + port);
    }

    @BeforeEach
    void clearData() throws Exception {server.clearDatabase();}

    @AfterAll
    static void stopServer() {
        new_server.stop();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }


    @Test
    public void registerSuccess() throws Exception{
        var authData = server.register(new RegisterRequest("newUser", "password", "newUser@example.com"));
        Assertions.assertNotNull(authData.authToken());
    }

    @Test
    public void registerFailure() {
        try {
            server.register(new RegisterRequest("newUser", "password", "newUser@example.com"));
            server.register(new RegisterRequest("newUser", "password", "newUser@example.com"));
        } catch (Exception e) {
            Assertions.assertTrue(e.getMessage().contains("403"), "Error indicates name duplicated");
        }
    }

    @Test
    public void loginSuccess() throws Exception{
        server.register(new RegisterRequest("newUser", "password", "newUser@example.com"));
        var AuthData = server.login(new LoginRequest("newUser","password"));
        Assertions.assertNotNull(AuthData);
    }

    @Test
    public void loginFailure() throws Exception{
        try{
            server.login(new LoginRequest("unregisteredUser", "password"));
        } catch (Exception e) {
            Assertions.assertTrue(e.getMessage().contains("401"), "Error indicates user unregistered");
        }
    }

    @Test
    public void logoutSuccess() throws Exception {
        try {
            var AuthData = server.register(new RegisterRequest("newUser", "password", "newUser@example.com"));
            server.logout(AuthData.authToken());
            server.listGames(AuthData.authToken());
        } catch (Exception e) {
            Assertions.assertTrue(e.getMessage().contains("401"), "Error indicates user unregistered");
        }
    }

    @Test
    public void logoutFailure() throws Exception{
        try {
            var AuthData = server.login(new LoginRequest("newUser", "password"));
            server.logout("fake-authtoken");
        } catch (Exception e) {
            Assertions.assertTrue(e.getMessage().contains("401"), "Error indicates user unregistered");
        }
    }

    @Test
    public void CreateGameSuccess() throws  Exception{
        try {
            server.clearDatabase();
            var AuthData = server.register(new RegisterRequest("newUser", "password", "mail@email.com"));
            server.createGame("game", AuthData.authToken());
            Assertions.assertNotNull(server.listGames(AuthData.authToken()));
        } catch (Exception e) {
            Assertions.assertTrue(e.getMessage().contains("401"), "Error indicates user unregistered");
        }
    }


    @Test
    public void CreateGameFailure() throws  Exception{
        try {
            server.clearDatabase();
            var AuthData = server.register(new RegisterRequest("newUser", "password", "email@mail.com"));
            server.createGame("game", "bad auth");
        } catch (Exception e) {
            Assertions.assertTrue(e.getMessage().contains("401"), "Error indicates user unauthorized");
        }
    }

    @Test
    public void ListGamesSuccess() throws  Exception{
        try{
            var AuthData = server.login(new LoginRequest("newUser", "password"));
            server.createGame("testGame", AuthData.authToken());
            Assertions.assertNotNull(server.listGames(AuthData.authToken()));
        } catch (Exception e) {
            Assertions.assertTrue(e.getMessage().contains("401"), "Error indicates user unregistered");
        }
    }

    @Test
    public void ListGamesFailure() throws Exception{
        server.clearDatabase();
        var AuthData = server.register(new RegisterRequest("newUser", "password","email@gmail.com"));
        Assertions.assertNull(server.listGames(AuthData.authToken()));
    }

    @Test
    public void JoinGameSucccess() throws Exception{
        server.clearDatabase();
        var AuthData = server.login(new LoginRequest("newUser", "password"));
        server.createGame("newTest", AuthData.authToken());
        server.joinGame("WHITE", 1);
//        Assertions.assertDoesNotThrow(Exception);
    }

    @Test
    public void JoinGameFailure() throws Exception{
        try {
            server.clearDatabase();
            server.joinGame("WHITE", 1);
        } catch (Exception e) {
            Assertions.assertTrue(e.getMessage().contains("404"), "Error indicates user unregistered");
        }
    }









}
