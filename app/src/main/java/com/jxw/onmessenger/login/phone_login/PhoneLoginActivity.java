package com.jxw.onmessenger.login.phone_login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.jxw.onmessenger.R;
import com.jxw.onmessenger.home.views.MainActivity;
import com.jxw.onmessenger.login.LoginActivity;

import java.util.concurrent.TimeUnit;

public class PhoneLoginActivity extends AppCompatActivity {
    private TextView enterPhoneHintText, preferEmailAuthLink;
    private Button sendVerificationCodeBtn, verifyCodeBtn;
    private EditText phoneNumberInput, verifyCodeInput;
    private String phoneVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks phoneVerificationCallbacks;
    private Spinner spinner;
    private ProgressBar progressBar;
    private ArrayAdapter<String> arrayAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);

        initProperties();
        
        sendVerificationCodeBtn.setOnClickListener(view -> checkInputNotNull());
        verifyCodeBtn.setOnClickListener(view -> checkCodeNotNull());
        preferEmailAuthLink.setOnClickListener(view -> goToLogin());
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, CountryCodeData.countryNames);
        spinner.setAdapter(arrayAdapter);
    }

    private void goToLogin() {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
    }

    private void checkCodeNotNull() {
        String code = verifyCodeInput.getText().toString().trim();
        if (code.isEmpty()) {
            verifyCodeInput.setError("Enter a valid code");
            verifyCodeInput.requestFocus();
        } else {
            verifyCode(code);
        }

    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(phoneVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void checkInputNotNull() {
        String phoneNumber = phoneNumberInput.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() < 10) {
            Toast.makeText(this, "Please enter a valid phone number", Toast.LENGTH_LONG).show();
            return;
        }
        String countryCode = CountryCodeData.countryAreaCodes[spinner.getSelectedItemPosition()];
        sendVerificationCode("+" + countryCode + phoneNumber);
    }

    private void sendVerificationCode(String phoneNumber) {
        progressBar.setVisibility(View.VISIBLE);
        sendVerificationCodeBtn.setEnabled(false);

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                phoneVerificationCallbacks);        // OnVerificationStateChangedCallbacks


    }

    private void initProperties() {
        spinner = findViewById(R.id.country_code_spinner);
        progressBar = findViewById(R.id.progressbar);

        enterPhoneHintText = findViewById(R.id.enter_phone_number_hint_text);
        preferEmailAuthLink = findViewById(R.id.prefer_email_login_link);
        sendVerificationCodeBtn = findViewById(R.id.send_phone_verification_button);
        verifyCodeBtn = findViewById(R.id.verify_auth_code_button);
        phoneNumberInput = findViewById(R.id.phone_number_input);
        verifyCodeInput = findViewById(R.id.verification_code_input);

        phoneVerificationCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                progressBar.setVisibility(View.INVISIBLE);
                sendVerificationCodeBtn.setEnabled(true);
                sendVerificationCodeBtn.setText("Resend");

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Toast.makeText(PhoneLoginActivity.this,
                            "Invalid Phone. Please check and try again",
                            Toast.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Toast.makeText(PhoneLoginActivity.this,
                            "Oops!. This is not your fault, it is ours. We are working to get it" +
                                    " fixed as not time.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PhoneLoginActivity.this,
                            "Oops!. Something went wrong, please try again.",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {

                // Save verification ID and resending token so we can use them later
                phoneVerificationId = verificationId;

                Toast.makeText(PhoneLoginActivity.this, "Code Sent", Toast.LENGTH_SHORT).show();

                progressBar.setVisibility(View.INVISIBLE);
                sendVerificationCodeBtn.setVisibility(View.INVISIBLE);
                enterPhoneHintText.setText(getString(R.string.verify_code_header_text));

                verifyCodeBtn.setVisibility(View.VISIBLE);
                LinearLayout phoneNumberContainer = findViewById(R.id.phone_inputs_container);
                phoneNumberContainer.setVisibility(View.INVISIBLE);
                verifyCodeInput.setVisibility(View.VISIBLE);
            }
        };
        
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        progressBar.setVisibility(View.VISIBLE);
        verifyCodeBtn.setEnabled(false);

        FirebaseAuth fbAuth = FirebaseAuth.getInstance();
        fbAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            progressBar.setVisibility(View.INVISIBLE);
                            verifyCodeBtn.setEnabled(true);

                            FirebaseUser user = task.getResult().getUser();
                            Toast.makeText(PhoneLoginActivity.this, user.getPhoneNumber(), Toast.LENGTH_SHORT).show();

                            goToMainActivity();
                        } else {
                            progressBar.setVisibility(View.INVISIBLE);
                            verifyCodeBtn.setEnabled(true);

                            // Sign in failed, display a message and update the UI
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(PhoneLoginActivity.this, "Invalid Code", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void goToMainActivity() {
        Intent mainActivityIntent = new Intent(PhoneLoginActivity.this, MainActivity.class);
        mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainActivityIntent);
        finish();
    }
}
