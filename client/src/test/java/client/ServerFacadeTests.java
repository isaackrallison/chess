package client;

import com.sun.source.tree.AssertTree;
import model.RegisterRequest;
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
        Assertions.assertNotNull(authData);
    }

    @Test
    public void registerFailure() {
        try {
            server.register(new RegisterRequest("newUser", "password", "newUser@example.com"));
            var authData = server.register(new RegisterRequest("newUser", "password", "newUser@example.com"));
        } catch (Exception e) {
            Assertions.assertTrue(e.getMessage().contains("403"), "Error indicates name duplicated");
        }
    }



}
