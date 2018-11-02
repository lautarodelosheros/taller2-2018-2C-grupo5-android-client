package com.comprame.mypurchases;

import android.arch.lifecycle.ViewModel;
import android.text.Layout;
import android.view.Gravity;

import com.comprame.R;

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

    public int getShape() {
        if (user.equals(chatMessage.getUserName()))

            return R.drawable.chat_out_shape;
        else
            return R.drawable.chat_in_shape;
    }

    public int getGravity() {
        if (user.equals(chatMessage.getUserName()))
            return Gravity.START;
        else
            return Gravity.END;
    }

}
