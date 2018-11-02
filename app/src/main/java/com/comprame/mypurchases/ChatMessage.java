package com.comprame.mypurchases;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

class ChatMessage {
    private String userName;
    private String message;
    private String timestamp;

    public ChatMessage(String userName, String message, String timestamp) {
        this.userName = userName;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
