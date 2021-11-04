package com.example.chatapp;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ChatMessage {

    private String messageText;
    private String messageUser;
    private String messageTime;
    private String messageDate;
    private String messageUserID;

    public ChatMessage(){}
    public ChatMessage(String messageText, String messageUser) {
        this.messageText = messageText;
        this.messageUser = messageUser;

        // Initialize to current time
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Calendar cal = Calendar.getInstance();

        messageTime = timeFormat.format(cal.getTime());
        messageDate = dateFormat.format(cal.getTime());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        messageUserID = user.getUid();
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public String getMessageDate() { return messageDate; }

    public void setMessageDate(String messageDate) { this.messageDate = messageDate; }

    public String getMessageUserID() { return messageUserID; }

    public void setMessageUserID(String messageUserID) { this.messageUserID = messageUserID; }
}
