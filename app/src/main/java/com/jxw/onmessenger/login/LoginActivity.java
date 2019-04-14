package com.jxw.onmessenger.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.jxw.onmessenger.MainActivity;
import com.jxw.onmessenger.R;
import com.jxw.onmessenger.register.RegisterActivity;

public class LoginActivity extends AppCompatActivity {
    private FirebaseUser currentUser;
    private Button loginButton, phoneLoginButton;
    private EditText emailInputField, passwordInputField;
    private TextView forgetPasswordLink, notRegisteredLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeFields();
        notRegisteredLink.setOnClickListener(view -> {
            Intent registerActivityIntent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(registerActivityIntent);
        });
    }

    private void initializeFields() {
        loginButton = findViewById(R.id.login_button);
        phoneLoginButton = findViewById(R.id.phone_login_option_button);

        emailInputField = findViewById(R.id.email_input);
        passwordInputField = findViewById(R.id.password_input);

        forgetPasswordLink = findViewById(R.id.forget_password_link);
        notRegisteredLink = findViewById(R.id.not_registered_link);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (currentUser != null) {
            sendUserToMainActivity();
        }
    }

    private void sendUserToMainActivity() {
        Intent mainActivityIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(mainActivityIntent);
    }
}
