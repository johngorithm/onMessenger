package com.jxw.onmessenger.settings;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jxw.onmessenger.home.views.MainActivity;
import com.jxw.onmessenger.R;
import com.jxw.onmessenger.models.User;


public class SettingsActivity extends AppCompatActivity implements SettingsView{
    private Button updateButton;
    private EditText statusInput, usernameInput;
    // profileImage field expected

    private DatabaseReference fbRootRef;
    private FirebaseUser currentUser;
    private ProgressDialog progressDialog;

    private SettingsPresenter settingsPresenter;

    View.OnClickListener profileUpdateClickListener = view -> {
        String username = usernameInput.getText().toString();
        String status = statusInput.getText().toString();

        if (!TextUtils.isEmpty(username)) {
            initiateProfileUpdate(username, status, null);
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

        settingsPresenter.fetchProfile();
    }

    private void initiateProfileUpdate(String username, @Nullable String status, @Nullable String displayPicture) {
        progressDialog.setTitle("Updating Profile");
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();

        settingsPresenter.updateProfile(username, status, displayPicture);
    }

    private void goToMainActivity() {
        Intent mainActivityIntent = new Intent(SettingsActivity.this, MainActivity.class);
        mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainActivityIntent);
        finish();
    }

    private void initializeFields() {
        updateButton = findViewById(R.id.update_profile_btn);
        usernameInput = findViewById(R.id.username_input);
        statusInput = findViewById(R.id.status_input);

        progressDialog = new ProgressDialog(SettingsActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);

        settingsPresenter = new SettingsPresenter(this);
    }

    @Override
    public void handleError(String message) {
        progressDialog.dismiss();
        Toast.makeText(SettingsActivity.this, message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onFetchProfileSuccess(User user) {
        usernameInput.setText(user.getUsername());
        statusInput.setText(user.getStatusInfo());
        progressDialog.dismiss();
    }

    @Override
    public void onUpdateProfileSuccess() {
        progressDialog.dismiss();
        Toast.makeText(SettingsActivity.this, "Update is successful", Toast.LENGTH_SHORT).show();
        if (getIntent().getFlags() == Intent.FLAG_ACTIVITY_CLEAR_TASK + Intent.FLAG_ACTIVITY_NEW_TASK){
            goToMainActivity();
        }
    }
}
