package com.jxw.onmessenger.settings;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jxw.onmessenger.models.User;
import com.jxw.onmessenger.services.FirebaseService;
import com.jxw.onmessenger.utils.Redirection;

import java.util.HashMap;
import java.util.Map;

class SettingsPresenter {
    private Context context;
    private SettingsView settingsView;
    private DatabaseReference fbRootRef;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private StorageReference fbStorageRootRef;

    /**
     * Constructor.
     */
    SettingsPresenter(SettingsView view) {
        this.settingsView = view;
        context = (Context) view;
        fbRootRef = FirebaseService.getFbRootRef();
        firebaseAuth = FirebaseService.getFbAuthService();
        currentUser = firebaseAuth.getCurrentUser();
        fbStorageRootRef = FirebaseService.getFbStorageRef();
    }

    void fetchProfile() {
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

    void updateProfile(String username, @Nullable String status, @Nullable String displayPicture) {

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

    void saveProfileImage(Uri imageUri) {
        StorageReference filePath = fbStorageRootRef.child("ProfileImages").child(currentUser.getUid() + ".png");
        Task<UploadTask.TaskSnapshot> uploadTask = filePath.putFile(imageUri);

        Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    String msg = task.getException().getMessage();
                    settingsView.handleError(msg);
                }
                return filePath.getDownloadUrl();
            }
        });

        uriTask.addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Image Successfully updated", Toast.LENGTH_LONG).show();
                    Uri downloadUri = task.getResult();
                    String imgUrl = downloadUri.toString();
                    settingsView.handleProfileImageUpdateSuccess(imgUrl);
                    updateProfileImage(imgUrl);
                } else if (task.getException() != null){
                    String msg = task.getException().getMessage();
                    settingsView.handleError(msg);
                } else {
                    settingsView.handleError("Unable to get image link");
                }
            }
        });
    }

    private void updateProfileImage(String imageUrl) {
        HashMap<String, Object> imgMap = new HashMap<>();
        imgMap.put("displayPicture", imageUrl);
        fbRootRef.child("Users").child(currentUser.getUid()).updateChildren(imgMap);
    }
}
