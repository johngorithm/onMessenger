package com.jxw.onmessenger.login;

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
import com.jxw.onmessenger.login.phone_login.PhoneLoginActivity;
import com.jxw.onmessenger.register.RegisterActivity;

public class LoginActivity extends AppCompatActivity implements LoginView {
    private Button loginButton, phoneLoginButton;
    private EditText emailInputField, passwordInputField;
    private TextView forgetPasswordLink, notRegisteredLink;
    private ProgressDialog progressDialog;
    private LoginPresenter loginPresenter;

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

        initializeFields();
        notRegisteredLink.setOnClickListener(view -> sendUserToRegisterActivity());
        loginButton.setOnClickListener(loginClickListener);
        phoneLoginButton.setOnClickListener(view -> goToPhoneLogin());
    }

    private void goToPhoneLogin() {
        Intent phoneAuthActivityIntent = new Intent(LoginActivity.this, PhoneLoginActivity.class);
        startActivity(phoneAuthActivityIntent);
    }

    private void loginUser(String email, String password) {
        progressDialog.setTitle("Authenticating ...");
        progressDialog.setMessage("Please wait");
        progressDialog.show();

        loginPresenter.authenticateUser(email, password);
    }

    private void goToMainActivity() {
        Intent mainActivityIntent = new Intent(LoginActivity.this, MainActivity.class);
        mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainActivityIntent);
        finish();
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

        // PRESENTER
        loginPresenter = new LoginPresenter(this);
    }

    @Override
    public void onLoginSuccess() {
        progressDialog.dismiss();
        Toast.makeText(LoginActivity.this,
                "Login Successful", Toast.LENGTH_SHORT).show();
        goToMainActivity();
    }

    @Override
    public void handleError(String message) {
        progressDialog.dismiss();
        Toast.makeText(LoginActivity.this,
                "Error: "+message, Toast.LENGTH_SHORT).show();
    }
}
