package com.jxw.onmessenger.settings;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.jxw.onmessenger.models.User;
import com.jxw.onmessenger.services.FirebaseService;
import com.jxw.onmessenger.utils.Redirection;

import java.util.Map;

public class SettingsPresenter {
    private Context context;
    private SettingsView settingsView;
    private DatabaseReference fbRootRef;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;

    /**
     * Constructor.
     */
    public SettingsPresenter(SettingsView view) {
        this.settingsView = view;
        context = (Context) settingsView;
        fbRootRef = FirebaseService.getFbRootRef();
        firebaseAuth = FirebaseService.getFbAuthService();
        currentUser = firebaseAuth.getCurrentUser();
    }

    public void fetchProfile() {
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            fbRootRef.child("Users").child(currentUserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        User user = dataSnapshot.getValue(User.class);
                        settingsView.onFetchProfileSuccess(user);
                    } else {
                        String message = "Oops... No Data Found";
                        settingsView.handleError(message);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    String message = databaseError.getMessage();
                    settingsView.handleError(message);
                }
            });
        } else {
            // Redirect to Login
            Redirection.sendUserToLogin(context);
        }
    }

    public void updateProfile(String username, @Nullable String status, @Nullable String displayPicture) {

        if (currentUser != null) {
            String userId = currentUser.getUid();
            User user = new User(userId, username, status, displayPicture);
            Map<String, Object> updates = user.toMap();

            fbRootRef.child("Users").child(userId).updateChildren(updates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                settingsView.onUpdateProfileSuccess();
                            } else if (task.getException() != null){
                                String errMessage = task.getException().getMessage();
                                settingsView.handleError(errMessage);
                            } else {
                                String message = "Error: Failed to update profile. Please try again";
                                settingsView.handleError(message);
                            }
                        }
                    });
        } else {
            // redirect to login
            Redirection.sendUserToLogin(context);
        }
    }
}
