package com.jxw.onmessenger.home;

import android.support.annotation.NonNull;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.jxw.onmessenger.services.FirebaseService;


public class HomePresenter {
    private HomeView homeView;
    private DatabaseReference fbRootRef;
    private FirebaseUser currentUser;

    public HomePresenter(HomeView view) {
        this.homeView = view;
        fbRootRef = FirebaseService.getFbRootRef();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

    }

    public void verifyUsernameExists() {
        String currentUserId = currentUser.getUid();

        fbRootRef.child("Users").child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild("username")
                        && dataSnapshot.child("username").getValue() == null) {
                    homeView.handleUsernameVerificationResult(false);
                } else {
                    homeView.handleUsernameVerificationResult(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                String message = databaseError.getMessage();
                homeView.handleNetworkRequestError(message);
            }
        });
    }

    public void createNewGroup(String groupName){
        String newGroupKey = fbRootRef.child("Groups").push().getKey();

        fbRootRef.child("Groups").child(newGroupKey).setValue(groupName)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            homeView.onGroupCreationSuccess(groupName);
                        } else {
                            if (task.getException() != null) {
                                String errorMsg = task.getException().getMessage();
                                homeView.handleNetworkRequestError(errorMsg);
                            }
                        }
                    }
                });
    }
}
