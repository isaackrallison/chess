//package ui;
//
//import com.google.gson.Gson;
//import exception.ResponseException;
//import model.*;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.net.HttpURLConnection;
//import java.net.URI;
//import java.net.URL;
//import java.util.List;
//
//public class ServerFacade {
//
//    private final String serverUrl;
//
//    public ServerFacade(String url) {
//        serverUrl = url;
//    }
//
//    public RegisterResult register(RegisterRequest request) throws ResponseException {
//        var path = "/user";
//        return this.makeRequest("POST", path, request, RegisterResult.class);
//    }
//
//    public LoginResult login(LoginRequest request) throws ResponseException {
//        var path = "/session";
//        return this.makeRequest("POST", path, request, LoginResult.class);
//    }
//
//    public void logout(String authToken) throws ResponseException {
//        var path = "/session";
//        this.makeRequest("DELETE", path, authToken, null);
//    }
//
//    public List<GameData> listGames(String authToken) throws ResponseException {
//        var path = "/game";
//        record listGameResponse(List<GameData> games) {
//        }
//        var response = this.makeRequest("GET", path, authToken, listGameResponse.class);
//        return response.games();
//    }
//
//    public int createGame(String gameName, String authToken) throws ResponseException {
//        var path = "/game";
//        record CreateGameRequest(String gameName, String authToken) {}
//        CreateGameRequest request = new CreateGameRequest(gameName, authToken);
//        return this.makeRequest("POST", path, request, Integer.class);
//    }
//
//    public void joinGame(String color, int gameIdNum) throws ResponseException {
//        var path = String.format("/game/%s", gameIdNum);
//        JoinGameRequest request = new JoinGameRequest(color, gameIdNum);
//        this.makeRequest("PUT", path, request, null);
//    }
//
//    public  void clearDatabase() throws ResponseException {
//        var path = "/db";
//        this.makeRequest("DELETE", path, null, null);
//    }
//
//    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
//        try {
//            URL url = (new URI(serverUrl + path)).toURL();
//            HttpURLConnection http = (HttpURLConnection) url.openConnection();
//            http.setRequestMethod(method);
//            http.setDoOutput(true);
//
//            writeBody(request, http);
//            http.connect();
//            throwIfNotSuccessful(http);
//            return readBody(http, responseClass);
//        } catch (Exception ex) {
//            throw new ResponseException(500, ex.getMessage());
//        }
//    }
//
//    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
//        if (request != null) {
//            if (request instanceof String authToken) {
//                http.addRequestProperty("Authorization", authToken);
//            } else {
//                http.addRequestProperty("Content-Type", "application/json");
////                String reqData = new Gson().toJson(request);
//            }
//            String reqData = new Gson().toJson(request);
////            if (request instanceof String authToken) {
////                http.addRequestProperty("Authorization", authToken);
////            }
//            try (OutputStream reqBody = http.getOutputStream()) {
//                reqBody.write(reqData.getBytes());
//            }
//        }
//    }
//
//    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
//        var status = http.getResponseCode();
//        if (!isSuccessful(status)) {
//            throw new ResponseException(status, "failure: " + status);
//        }
//    }
//
//    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
//        T response = null;
//        if (http.getContentLength() < 0) {
//            try (InputStream respBody = http.getInputStream()) {
//                InputStreamReader reader = new InputStreamReader(respBody);
//                if (responseClass != null) {
//                    response = new Gson().fromJson(reader, responseClass);
//                }
//            }
//        }
//        return response;
//    }
//
//
//    private boolean isSuccessful(int status) {
//        return status / 100 == 2;
//    }
//}

package ui;

import com.google.gson.Gson;
import exception.ResponseException;
import model.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.List;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public RegisterResult register(RegisterRequest request) throws ResponseException {
        var path = "/user";
        return this.makeRequest("POST", path, request, RegisterResult.class, null);
    }

    public LoginResult login(LoginRequest request) throws ResponseException {
        var path = "/session";
        return this.makeRequest("POST", path, request, LoginResult.class, null);
    }

    public void logout(String authToken) throws ResponseException {
        var path = "/session";
        this.makeRequest("DELETE", path, null, null, authToken);
    }

    public List<GameData> listGames(String authToken) throws ResponseException {
        var path = "/game";
        record listGameResponse(List<GameData> games) {}
        var response = this.makeRequest("GET", path, null, listGameResponse.class, authToken);
        return response.games();
    }

//    public int createGame(String gameName, String authToken) throws ResponseException {
//        var path = "/game";
//        record CreateGameRequest(String gameName) {}
//        CreateGameRequest request = new CreateGameRequest(gameName);
//        return this.makeRequest("POST", path, request, Integer.class, authToken);
//    }
    public int createGame(String gameName, String authToken) throws ResponseException {
        var path = "/game";
        record CreateGameRequest(String gameName) {}
        record CreateGameResponse(int gameId) {}  // Adjusted to match expected response

        CreateGameRequest request = new CreateGameRequest(gameName);
        CreateGameResponse response = this.makeRequest("POST", path, request, CreateGameResponse.class, authToken);
        return response.gameId();  // Extract the gameId field from the response
    }

    public void joinGame(String color, int gameIdNum) throws ResponseException {
        var path = String.format("/game/%s", gameIdNum);
        JoinGameRequest request = new JoinGameRequest(color, gameIdNum);
        this.makeRequest("PUT", path, request, null, null);
    }

    public void clearDatabase() throws ResponseException {
        var path = "/db";
        this.makeRequest("DELETE", path, null, null, null);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String authToken) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            if (authToken != null) {
                http.setRequestProperty("Authorization", authToken);
            }

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, "failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}

