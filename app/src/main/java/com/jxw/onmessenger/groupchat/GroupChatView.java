package com.jxw.onmessenger.groupchat;

import com.jxw.onmessenger.models.Message;


interface GroupChatView {
    void handNetworkError(String message);
    void displayMessage(Message messages);
}
