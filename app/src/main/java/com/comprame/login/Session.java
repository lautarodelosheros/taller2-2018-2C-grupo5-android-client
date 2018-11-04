package com.comprame.login;

public class Session {
    private static final Session session = new Session();
    private String token;

    private Session() {}

    public static Session getInstance() {
        return session;
    }

    public String getSessionToken() {
        return session.token;
    }

    public void setSessionToken(String token) {
        session.token = token;
    }
}
