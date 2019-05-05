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

import com.bumptech.glide.Glide;
import com.jxw.onmessenger.home.views.MainActivity;
import com.jxw.onmessenger.R;
import com.jxw.onmessenger.models.User;

import de.hdodenhof.circleimageview.CircleImageView;


public class SettingsActivity extends AppCompatActivity implements SettingsView{
    private Button updateButton;
    private EditText statusInput, usernameInput;
    private CircleImageView profileImage;

    private ProgressDialog progressDialog;

    private SettingsPresenter settingsPresenter;
    private static final int GALLERY_CODE = 1;

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

        Toolbar appToolbar = findViewById(R.id.settings_app_bar);
        setSupportActionBar(appToolbar);
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initializeFields();
        updateButton.setOnClickListener(profileUpdateClickListener);
        profileImage.setOnClickListener(view -> selectImage());

        fetchProfileData();
    }

    private void selectImage() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT).setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_CODE);
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
        profileImage = findViewById(R.id.profile_image);

        progressDialog = new ProgressDialog(SettingsActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);

        settingsPresenter = new SettingsPresenter(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK && data != null) {
            progressDialog.setMessage("Updating display picture ...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            settingsPresenter.saveProfileImage(data.getData());
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }

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
        String displayImage = user.getDisplayPicture();

        if (displayImage != null && !displayImage.isEmpty()) {
            Glide.with(this)
                    .asBitmap()
                    .load(user.getDisplayPicture())
                    .into(profileImage);
        }

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

    @Override
    public void handleProfileImageUpdateSuccess(String imgUrl) {
        progressDialog.dismiss();
        // TODO: prevent from crashing if activity isDestroyed
        if (imgUrl != null && !imgUrl.isEmpty()) {
            Glide.with(this)
                    .asBitmap()
                    .load(imgUrl)
                    .into(profileImage);
        }
    }
}
