package com.jxw.onmessenger.models;

import android.support.annotation.NonNull;

import com.google.firebase.database.IgnoreExtraProperties;
import java.util.Calendar;

@IgnoreExtraProperties
public class Message {

    private String uid;
    private String chatId;
    private User sender;
    private String message;
    private long createdAt;

    public Message() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Message(String uid, String chatId, String message, @NonNull User user) {
        this.uid = uid;
        this.chatId = chatId;
        this.message = message;
        this.sender = user;
        this.createdAt = Calendar.getInstance().getTime().getTime();
    }

    public String getUid() {
        return uid;
    }

    public String getChatId() {
        return chatId;
    }

    public User getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public long getCreatedAt() {
        return createdAt;
    }
}
