package com.comprame.mypurchases;

import android.arch.lifecycle.ViewModel;

public class ChatMessageItemViewModel extends ViewModel {
    public final ChatMessage chatMessage;
    private String user;

    public ChatMessageItemViewModel(ChatMessage chatMessage, String user) {
        this.chatMessage = chatMessage;
        this.user = user;
    }

    public String getUserName() {
        return chatMessage.getUserName();
    }

    public String getMessage() {
        return chatMessage.getMessage();
    }

    public String getTimestamp() {
        return chatMessage.getTimestamp();
    }

    public boolean isTheUser() {
        return user.equals(chatMessage.getUserName());
    }

}
