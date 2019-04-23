package com.jxw.onmessenger.login;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.jxw.onmessenger.services.FirebaseService;

class LoginPresenter {
    private LoginView loginView;
    private DatabaseReference fbRootRef;
    private FirebaseAuth firebaseAuth;

    /**
     * Constructor.
     */
    LoginPresenter(LoginView view) {
        this.loginView = view;
        fbRootRef = FirebaseService.getFbRootRef();
        firebaseAuth = FirebaseService.getFbAuthService();
    }

    void authenticateUser(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loginView.onLoginSuccess();

                        } else {
                            if (task.getException() != null) {
                                String message = task.getException().getMessage();
                                loginView.handleError(message);
                            }
                        }
                    }
                });
    }
}
