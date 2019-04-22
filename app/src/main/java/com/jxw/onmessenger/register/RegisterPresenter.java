package com.jxw.onmessenger.register;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.jxw.onmessenger.models.User;
import com.jxw.onmessenger.services.FirebaseService;

public class RegisterPresenter {
    private RegisterView registerView;
    private DatabaseReference fbRootRef;
    private FirebaseAuth firebaseAuth;


    /**
     * Constructor.
     */
    public RegisterPresenter(RegisterView view) {
        this.registerView = view;
        fbRootRef = FirebaseService.getFbRootRef();
        firebaseAuth = FirebaseService.getFbAuthService();
    }

    void registerNewUser(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String currentUserId = task.getResult().getUser().getUid();
                            saveUserData(currentUserId);
                            registerView.onRegistrationSuccess();
                        } else {
                            if (task.getException() != null) {
                                String message = task.getException().getMessage();
                                registerView.onRegistrationFailure(message);
                            } else {
                                String message = "Oop.. Failed to create account. Please try again";
                                registerView.onRegistrationFailure(message);
                            }
                        }
                    }
                });
    }

    private void saveUserData(String userId) {
        User newUser = new User(userId, null, null, null);
        fbRootRef.child("Users").child(userId).setValue(newUser);
    }
}
