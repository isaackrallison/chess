package service;

import java.util.UUID;

public class AuthTokenCreator {
    private final String authToken;
    private final String username;

    // Constructor generates a unique auth token
    public AuthTokenCreator(String username) {
        this.authToken = generateToken();
        this.username = username;
    }

    // Generate a random UUID as the token
    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getUsername() {
        return username;
    }
}

