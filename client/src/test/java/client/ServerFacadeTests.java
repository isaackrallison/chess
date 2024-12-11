package client;

import model.*;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;

import java.util.ArrayList;
import java.util.List;


public class ServerFacadeTests {

    private static Server fascade;
    static ServerFacade server;

    @BeforeAll
    public static void init() {
        fascade = new Server();
        var port = fascade.run(0);
        server = new ServerFacade("http://localhost:" + port);
        System.out.println("Started test HTTP server on " + port);
    }

    @BeforeEach
    void clearData() throws Exception {
        server.clearDatabase();;
    }

    @AfterAll
    static void stopServer() {
        fascade.stop();
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
        var authData = server.login(new LoginRequest("newUser","password"));
        Assertions.assertNotNull(authData);
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
            var authData = server.register(new RegisterRequest("newUser", "password", "newUser@example.com"));
            server.logout(authData.authToken());
            server.listGames(authData.authToken());
        } catch (Exception e) {
            Assertions.assertTrue(e.getMessage().contains("401"), "Error indicates user unregistered");
        }
    }

    @Test
    public void logoutFailure() throws Exception{
        try {
            var authData = server.login(new LoginRequest("newUser", "password"));
            server.logout("fake-authtoken");
        } catch (Exception e) {
            Assertions.assertTrue(e.getMessage().contains("401"), "Error indicates user unregistered");
        }
    }

    @Test
    public void createGameSuccess() throws  Exception{
        try {
            var authData = server.register(new RegisterRequest("newUser", "password", "mail@email.com"));
            server.createGame("game", authData.authToken());
            Assertions.assertNotNull(server.listGames(authData.authToken()));
        } catch (Exception e) {
            Assertions.assertTrue(e.getMessage().contains("401"), "Error indicates user unregistered");
        }
    }


    @Test
    public void createGameFailure() throws  Exception{
        try {
            var authData = server.register(new RegisterRequest("newUser", "password", "email@mail.com"));
            server.createGame("game", "bad auth");
        } catch (Exception e) {
            Assertions.assertTrue(e.getMessage().contains("401"), "Error indicates user unauthorized");
        }
    }

    @Test
    public void listGamesSuccess() throws  Exception{
        try{
            var authData = server.login(new LoginRequest("newUser", "password"));
            server.createGame("testGame", authData.authToken());
            Assertions.assertNotNull(server.listGames(authData.authToken()));
        } catch (Exception e) {
            Assertions.assertTrue(e.getMessage().contains("401"), "Error indicates user unregistered");
        }
    }

    @Test
    public void listGamesFailure() throws Exception {
        server.clearDatabase();
        var authData = server.register(new RegisterRequest("newUser", "password", "email@gmail.com"));
        List<GameData> expectedList = new ArrayList<>();
        List<GameData> actualList = server.listGames(authData.authToken());
        Assertions.assertEquals(expectedList, actualList);
    }

    @Test
    public void joinGameSuccess() throws Exception {
        try {
            var authData = server.register(new RegisterRequest("newUser", "password", "email@gmail.com"));
            server.createGame("newTest", authData.authToken());
            server.createGame("newnewTest", authData.authToken());
            server.joinGame("WHITE", "a", authData.authToken());
            server.joinGame("WHITE", "a", authData.authToken());
        } catch (Exception e) {
            Assertions.assertTrue(e.getMessage().contains("4"), "Error indicates user unregistered");
        }
    }

    @Test
    public void joinGameFailure() throws Exception{
        try {
            server.joinGame("WHITE", "a","bad Auth");
        } catch (Exception e) {
            Assertions.assertTrue(e.getMessage().contains("401"), "Error indicates user unregistered");
        }
    }

}
