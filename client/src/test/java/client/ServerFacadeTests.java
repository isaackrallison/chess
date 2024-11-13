package client;

import com.sun.source.tree.AssertTree;
import model.RegisterRequest;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        facade = new ServerFacade("http://localhost:" + port);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }


    @Test
    public void registerSuccess() throws Exception{
        var authData = facade.register(new RegisterRequest("newUser", "password", "newUser@example.com"));
        Assertions.assertNotNull(authData);
    }

    @Test
    public void registerFailure() {
        try {
            facade.register(new RegisterRequest("newUser", "password", "newUser@example.com"));
            var authData = facade.register(new RegisterRequest("newUser", "password", "newUser@example.com"));
        } catch (Exception e) {
            Assertions.assertTrue(e.getMessage().contains("Already Exsists"), "Error indicates name duplicated");
        }
    }



}
