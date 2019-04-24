package com.jxw.onmessenger.login;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.jxw.onmessenger.R;
import com.jxw.onmessenger.models.User;
import com.jxw.onmessenger.services.FirebaseService;

import static android.support.constraint.Constraints.TAG;

class LoginPresenter {
    private LoginView loginView;
    private DatabaseReference fbRootRef;
    private FirebaseAuth firebaseAuth;
    private Activity activity;
    private SharedPreferences localStore;

    /**
     * Constructor.
     */
    LoginPresenter(LoginView view) {
        this.loginView = view;
        activity = (Activity) view;
        fbRootRef = FirebaseService.getFbRootRef();
        firebaseAuth = FirebaseService.getFbAuthService();

        localStore = activity.getSharedPreferences(
                activity.getString(R.string.share_pref_key), Context.MODE_PRIVATE);
    }

    void authenticateUser(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loginView.onLoginSuccess();
                            if (task.getResult() != null) {
                                String userId = task.getResult().getUser().getUid();
                                fetchUserData(userId);
                            }
                        } else {
                            if (task.getException() != null) {
                                String message = task.getException().getMessage();
                                loginView.handleError(message);
                            }
                        }
                    }
                });
    }

    void fetchUserData(String userId) {
        fbRootRef.child("Users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        SharedPreferences.Editor editor = localStore.edit();
                        editor.putString("username", user.getUsername());
                        editor.putString("userId", user.getUid());

                        editor.apply();
                    } // TODO: handle else case
                } // TODO: handle else case
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: "+databaseError.getMessage(), databaseError.toException());
            }
        });
    }
}
