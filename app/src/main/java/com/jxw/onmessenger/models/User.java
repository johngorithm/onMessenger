package com.jxw.onmessenger.models;

import android.support.annotation.Nullable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class User {
    public String uid;
    public String username;
    public String statusInfo;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public User(String uid, @Nullable String username, @Nullable String status) {
        this.uid = uid;
        this.username = username;
        this.statusInfo = status;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("uid", uid);
        result.put("username", username);
        result.put("statusInfo", statusInfo);
        return result;
    }
}
