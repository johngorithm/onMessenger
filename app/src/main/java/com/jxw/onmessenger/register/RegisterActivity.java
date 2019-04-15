package com.jxw.onmessenger.register;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.jxw.onmessenger.R;
import com.jxw.onmessenger.login.LoginActivity;

public class RegisterActivity extends AppCompatActivity {
    private EditText emailInputField, passwordInputField;
    private Button registerButton;
    private TextView alreadyHaveAccountLink;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    View.OnClickListener registerClickListener = view -> {
        String email = emailInputField.getText().toString();
        String password = passwordInputField.getText().toString();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            registerNewUser(email, password);
        } else {
            Toast.makeText(RegisterActivity.this,
                    "Empty Field Submitted", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initalizeViews();
        firebaseAuth = FirebaseAuth.getInstance();
        alreadyHaveAccountLink.setOnClickListener(view -> goToLogin());
        registerButton.setOnClickListener(registerClickListener);
    }

    private void goToLogin() {
        Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(loginIntent);
    }

    private void registerNewUser(String email, String password) {
        progressDialog.setTitle("Creating New Account");
        progressDialog.setMessage("Please Wait ...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            String userEmail = task.getResult().getUser().getEmail();
                            Toast.makeText(RegisterActivity.this,
                                    "Account Successfully Created" + userEmail,
                                    Toast.LENGTH_SHORT).show();
                            goToLogin();
                        } else {
                            progressDialog.dismiss();
                            if (task.getException() != null) {
                                String message = task.getException().getMessage();
                                Toast.makeText(RegisterActivity.this,
                                        "Error: "+message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void initalizeViews() {
        emailInputField = findViewById(R.id.register_email_input);
        passwordInputField = findViewById(R.id.register_password_input);

        registerButton = findViewById(R.id.register_button);
        alreadyHaveAccountLink = findViewById(R.id.already_have_account_link);

        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);
    }
}