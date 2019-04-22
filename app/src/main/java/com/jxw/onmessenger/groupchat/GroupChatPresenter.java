package com.jxw.onmessenger.groupchat;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.jxw.onmessenger.models.Message;
import com.jxw.onmessenger.services.FirebaseService;

import java.util.ArrayList;
import java.util.List;

class GroupChatPresenter {
    private GroupChatView groupChatView;
    private DatabaseReference fbRootRef;
    private FirebaseAuth fbAuth;


    GroupChatPresenter(GroupChatView view) {
        this.groupChatView = view;
        this.fbRootRef = FirebaseService.getFbRootRef();
        this.fbAuth = FirebaseService.getFbAuthService();
    }

    void fetchGroupChats(String chatId) {
        if (fbAuth.getCurrentUser() != null) {
            String currentUserId = fbAuth.getCurrentUser().getUid();

            fbRootRef.child("Messages").child(chatId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<Message> messages = new ArrayList<>();

                    for (DataSnapshot dataShot : dataSnapshot.getChildren()) {
                        Message message = dataShot.getValue(Message.class);
                        messages.add(message);
                    }
                    groupChatView.displayGroupChats(messages);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    String message = databaseError.getMessage();
                    groupChatView.handNetworkError(message);
                }
            });
        } else {
            // send user to login
        }
    }
}