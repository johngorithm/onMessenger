package com.jxw.onmessenger.register;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jxw.onmessenger.home.views.MainActivity;
import com.jxw.onmessenger.R;
import com.jxw.onmessenger.login.LoginActivity;

public class RegisterActivity extends AppCompatActivity implements RegisterView {
    private EditText emailInputField, passwordInputField;
    private Button registerButton;
    private TextView alreadyHaveAccountLink;
    private ProgressDialog progressDialog;
    private RegisterPresenter registerPreseter;

    View.OnClickListener registerClickListener = view -> {
        String email = emailInputField.getText().toString();
        String password = passwordInputField.getText().toString();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            startUserRegistration(email, password);
        } else {
            Toast.makeText(RegisterActivity.this,
                    "Empty Field Submitted", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initalizeProperties();
        alreadyHaveAccountLink.setOnClickListener(view -> goToLogin());
        registerButton.setOnClickListener(registerClickListener);
    }

    private void goToLogin() {
        Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(loginIntent);
    }

    private void startUserRegistration(String email, String password) {
        progressDialog.setTitle("Creating New Account");
        progressDialog.setMessage("Please Wait ...");
        progressDialog.show();

        registerPreseter.registerNewUser(email, password);
    }

    private void goToMainActivity() {
        Intent mainActivityIntent = new Intent(RegisterActivity.this, MainActivity.class);
        mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainActivityIntent);
        finish();
    }

    private void initalizeProperties() {
        emailInputField = findViewById(R.id.register_email_input);
        passwordInputField = findViewById(R.id.register_password_input);

        registerButton = findViewById(R.id.register_button);
        alreadyHaveAccountLink = findViewById(R.id.already_have_account_link);

        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);
        // PRESENTER
        registerPreseter = new RegisterPresenter(this);
    }

    @Override
    public void onRegistrationSuccess() {
        progressDialog.dismiss();
        Toast.makeText(RegisterActivity.this,
                "Account Successfully Created",
                Toast.LENGTH_SHORT).show();
        goToMainActivity();
    }

    @Override
    public void onRegistrationFailure(String message) {
        progressDialog.dismiss();
        Toast.makeText(RegisterActivity.this,
                "Error: "+message, Toast.LENGTH_SHORT).show();
    }
}