package com.jxw.onmessenger.models;

import android.support.annotation.Nullable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Group {

    private String uid;
    private String groupImage;
    private String groupName;
    private String lastMessage;
    private int newMessageCount;

    public Group() {

    }
    public Group(String uid, @Nullable String groupImage, String groupName, @Nullable  String lastMessage, @Nullable int newMessageCount) {
        this.groupImage = groupImage;
        this.groupName = groupName;
        this.lastMessage = lastMessage;
        this.newMessageCount = newMessageCount;
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public String getGroupImage() {
        return groupImage;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public int getNewMessageCount() {
        return newMessageCount;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("groupImage", groupImage);
        result.put("groupName", groupName);
        result.put("lastMessage", lastMessage);
        result.put("newMessageCount", newMessageCount);

        return result;
    }
}
