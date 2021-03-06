package com.jxw.onmessenger.models;

import android.support.annotation.Nullable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Group {

    private String uid;
    private String imageUrl;
    private String title;
    private String lastMessage;
    private int newMessageCount;
    private Map<String, String> members;
    private long createdAt;

    public Group() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Group(String uid, @Nullable String imageUrl, String title, @Nullable  String lastMessage, @Nullable int newMessageCount,
                 Map<String, String> members) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.lastMessage = lastMessage;
        this.newMessageCount = newMessageCount;
        this.uid = uid;
        this.members = members;
        this.createdAt = Calendar.getInstance().getTime().getTime();
    }

    public Map<String, String> getMembers() {
        return members;
    }

    public String getUid() {
        return uid;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public int getNewMessageCount() {
        return newMessageCount;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("title", title);
        result.put("imageUrl", imageUrl);
        result.put("lastMessage", lastMessage);
        result.put("newMessageCount", newMessageCount);

        return result;
    }
}
