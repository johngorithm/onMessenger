package com.jxw.onmessenger.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Chat {
    private String uid;
    private String chatType;
    private String title;
    private int memberLimit;
    private String imageUrl;
    private Message lastMessage;
    private long createdAt;

    public Chat() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Chat(String uid, String title, int memberLimit, String imageUrl, Message lastMessage,
                String chatType) {
        this.uid = uid;
        this.title = title;
        this.memberLimit = memberLimit;
        this.imageUrl = imageUrl;
        this.lastMessage = lastMessage;
        this.chatType = chatType;
        this.createdAt = Calendar.getInstance().getTime().getTime();
    }

    public String getUid() {
        return uid;
    }

    public String getChatType() {
        return chatType;
    }

    public String getTitle() {
        return title;
    }

    public int getMemberLimit() {
        return memberLimit;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("title", title);
        result.put("memberLimit", memberLimit);
        result.put("imageUrl", imageUrl);
        result.put("lastMessage", lastMessage);

        return result;
    }

}
