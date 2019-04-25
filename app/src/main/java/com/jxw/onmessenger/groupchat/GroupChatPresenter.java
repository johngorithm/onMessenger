package com.jxw.onmessenger.groupchat;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.jxw.onmessenger.R;
import com.jxw.onmessenger.models.Message;
import com.jxw.onmessenger.models.User;
import com.jxw.onmessenger.services.FirebaseService;
import com.jxw.onmessenger.utils.Redirection;


import static android.support.constraint.Constraints.TAG;

class GroupChatPresenter {
    private GroupChatView groupChatView;
    private DatabaseReference fbRootRef;
    private FirebaseAuth fbAuth;
    private Context context;
    private SharedPreferences localStore;


    GroupChatPresenter(GroupChatView view) {
        this.groupChatView = view;
        this.context = (Context) view;
        this.fbRootRef = FirebaseService.getFbRootRef();
        this.fbAuth = FirebaseService.getFbAuthService();

        Activity activity = (Activity) context;
        localStore = activity.getSharedPreferences(activity.getString(R.string.share_pref_key), Context.MODE_PRIVATE);
    }

    void fetchGroupChats(String chatId) {
        if (fbAuth.getCurrentUser() != null) {
            String currentUserId = fbAuth.getCurrentUser().getUid();

            fbRootRef.child("Messages").child(chatId).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Log.d(TAG, "onChildAdded: " + dataSnapshot.toString());
                    Message message = dataSnapshot.getValue(Message.class);
                    groupChatView.displayMessage(message);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    /**
                     * To be implemented when message update operation is in place.
                     */
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    /**
                     * To be implemented when message delete operation is in place.
                     */
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    /**
                     * To be implemented when message move operation is in place.
                     */
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    String message = databaseError.getMessage();
                    groupChatView.handNetworkError(message);
                }
            });
        } else {
            // send user to login
            Redirection.sendUserToLogin(context);
        }
    }

    void sendMessage(String message, String groupId) {
        if (fbAuth.getCurrentUser() != null) {
            FirebaseUser currentUser = fbAuth.getCurrentUser();
            String username = localStore.getString("username", "");

            String newMessageKey = fbRootRef.child("Messages").child(groupId).push().getKey();
            User user = new User(currentUser.getUid(), username, null, null);

            Message newMessage = new Message(newMessageKey, groupId, message, user);
            fbRootRef.child("Messages").child(groupId).child(newMessageKey).setValue(newMessage)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                ///
                            } else if (task.getException() != null) {
                                // tell user to resend message
                                String errMsg = task.getException().getMessage();
                                groupChatView.handNetworkError(errMsg);
                            } else {
                                String msg = "Unable to send message. Please retry";
                                groupChatView.handNetworkError(msg);
                            }
                        }
                    });
        } else {
            // send user to login
            Redirection.sendUserToLogin(context);
        }
    }
}