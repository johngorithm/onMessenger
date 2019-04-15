package com.jxw.onmessenger.login;

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
import com.google.firebase.auth.FirebaseUser;
import com.jxw.onmessenger.MainActivity;
import com.jxw.onmessenger.R;
import com.jxw.onmessenger.register.RegisterActivity;

public class LoginActivity extends AppCompatActivity {
    private FirebaseUser currentUser;
    private FirebaseAuth firebaseAuth;
    private Button loginButton, phoneLoginButton;
    private EditText emailInputField, passwordInputField;
    private TextView forgetPasswordLink, notRegisteredLink;
    private ProgressDialog progressDialog;

    View.OnClickListener loginClickListener = view -> {
        String email = emailInputField.getText().toString();
        String password = passwordInputField.getText().toString();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            loginUser(email, password);
        } else {
            Toast.makeText(LoginActivity.this,
                    "Empty Field Submitted", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        initializeFields();
        notRegisteredLink.setOnClickListener(view -> sendUserToRegisterActivity());
        loginButton.setOnClickListener(loginClickListener);
    }

    private void loginUser(String email, String password) {
        progressDialog.setTitle("Authenticating ...");
        progressDialog.setMessage("Please wait");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this,
                                    "Login Successful", Toast.LENGTH_SHORT).show();
                            gotToMainActivity();

                        } else {
                            progressDialog.dismiss();
                            if (task.getException() != null) {
                                String message = task.getException().getMessage();
                                Toast.makeText(LoginActivity.this,
                                        "Error: "+message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });


    }

    private void gotToMainActivity() {
        Intent mainActivityIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(mainActivityIntent);
    }

    private void sendUserToRegisterActivity() {
        Intent registerActivityIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(registerActivityIntent);
    }

    private void initializeFields() {
        loginButton = findViewById(R.id.login_button);
        phoneLoginButton = findViewById(R.id.phone_login_option_button);

        emailInputField = findViewById(R.id.email_input);
        passwordInputField = findViewById(R.id.password_input);

        forgetPasswordLink = findViewById(R.id.forget_password_link);
        notRegisteredLink = findViewById(R.id.not_registered_link);

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);
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
