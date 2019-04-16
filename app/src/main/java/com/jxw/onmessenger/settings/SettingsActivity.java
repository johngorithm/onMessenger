package com.jxw.onmessenger.settings;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jxw.onmessenger.home.views.MainActivity;
import com.jxw.onmessenger.R;
import com.jxw.onmessenger.models.User;

import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {
    private Button updateButton;
    private EditText statusInput, usernameInput;
    // profileImage field expected

    private DatabaseReference fbRootRef;
    private FirebaseUser currentUser;
    private ProgressDialog progressDialog;

    View.OnClickListener profileUpdateClickListener = view -> {
        String username = usernameInput.getText().toString();
        String status = statusInput.getText().toString();

        if (!TextUtils.isEmpty(username)) {
            updateProfile(username, status);
        } else {
            Toast.makeText(SettingsActivity.this,
                    "Username is required", Toast.LENGTH_SHORT).show();
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        fbRootRef = FirebaseDatabase.getInstance().getReference();


        Toolbar appToolbar = findViewById(R.id.settings_app_bar);
        setSupportActionBar(appToolbar);
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initializeFields();
        updateButton.setOnClickListener(profileUpdateClickListener);

        fetchProfileData();
    }

    private void fetchProfileData() {
        progressDialog.setMessage("Fetching Profile ...");
        progressDialog.show();

        String currentUserId = currentUser.getUid();
        fbRootRef.child("Users").child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                usernameInput.setText(user.username);
                statusInput.setText(user.statusInfo);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                Toast.makeText(SettingsActivity.this,
                        databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateProfile(String username, @Nullable String status) {
        progressDialog.setTitle("Updating Profile");
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();

        String userId = currentUser.getUid();
        User user = new User(userId, username, status);
        Map<String, Object> updates = user.toMap();

        fbRootRef.child("Users").child(userId).updateChildren(updates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(SettingsActivity.this, "Update is successful", Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog.dismiss();
                            if (task.getException() != null) {
                                String errMessage = task.getException().getMessage();
                                Toast.makeText(SettingsActivity.this, errMessage, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SettingsActivity.this,
                                        "Error: Failed to update profile. Please try again", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    private void initializeFields() {
        updateButton = findViewById(R.id.update_profile_btn);
        usernameInput = findViewById(R.id.username_input);
        statusInput = findViewById(R.id.status_input);

        progressDialog = new ProgressDialog(SettingsActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);
    }
}
