package com.comprame.mypurchases;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class ChatViewModel extends ViewModel {

    public final MutableLiveData<List<ChatMessage>> chatMessages;

    public ChatViewModel() {
        chatMessages = new MutableLiveData<>();
        chatMessages.setValue(new ArrayList<>());
    }

    public void observeForever(Observer<List<ChatMessage>> observer) {
        chatMessages.observeForever(observer);
    }

    public int size() {
        return chatMessages.getValue().size();
    }

    public void addChatMessage(ChatMessage chatMessage) {
        this.chatMessages.getValue().add(chatMessage);
        setChatMessages(this.chatMessages.getValue());
    }

    public void setChatMessages(List<ChatMessage> chatMessages) {
        this.chatMessages.setValue(chatMessages);
    }

    public void removeAllMessages() {
        this.chatMessages.getValue().clear();
    }
}
