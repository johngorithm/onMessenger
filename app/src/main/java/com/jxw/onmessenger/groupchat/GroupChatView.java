package com.jxw.onmessenger.groupchat;

import com.jxw.onmessenger.models.Message;

import java.util.List;

interface GroupChatView {
    void handNetworkError(String message);
    void displayGroupChats(List<Message> messages);
}
