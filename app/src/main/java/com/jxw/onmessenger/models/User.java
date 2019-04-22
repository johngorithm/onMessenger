package com.jxw.onmessenger.models;

import android.support.annotation.Nullable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class User {
    private String uid;
    private String username;
    private String statusInfo;
    private String displayPicture;
    private long createdAt;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public User(String uid, @Nullable String username, @Nullable String status, @Nullable String displayPicture) {
        this.uid = uid;
        this.username = username;
        this.statusInfo = status;
        this.displayPicture = displayPicture;
        this.createdAt = Calendar.getInstance().getTime().getTime();
    }

    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getStatusInfo() {
        return statusInfo;
    }

    public String getDisplayPicture() {
        return displayPicture;
    }
    public long getCreatedAt() {
        return createdAt;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("username", username);
        result.put("statusInfo", statusInfo);
        result.put("displayPicture", displayPicture);
        return result;
    }
}
