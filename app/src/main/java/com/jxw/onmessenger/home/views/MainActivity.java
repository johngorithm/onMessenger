package com.jxw.onmessenger.home.views;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.jxw.onmessenger.R;
import com.jxw.onmessenger.home.HomePresenter;
import com.jxw.onmessenger.home.HomeView;
import com.jxw.onmessenger.home.adapters.TabAccessAdapter;
import com.jxw.onmessenger.login.LoginActivity;
import com.jxw.onmessenger.settings.SettingsActivity;

public class MainActivity extends AppCompatActivity implements HomeView {
    private Toolbar appToolbar;
    private TabLayout mainTabLayout;
    private ViewPager mainViewPager;
    private TabAccessAdapter tabAccessAdapter;
    private HomePresenter homePresenter;

    private FirebaseUser currentUser;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        initializeProperties();


        setSupportActionBar(appToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("onMessenger");
        }

        tabAccessAdapter = new TabAccessAdapter(getSupportFragmentManager());
        mainViewPager.setAdapter(tabAccessAdapter);
        mainTabLayout.setupWithViewPager(mainViewPager);

        userCheck();
    }

    private void initializeProperties() {
        appToolbar = findViewById(R.id.main_page_toolbar);
        progressDialog = new ProgressDialog(MainActivity.this);
        // View Pager
        mainViewPager = findViewById(R.id.main_view_pager);
        // TabLayout
        mainTabLayout = findViewById(R.id.main_tabs);
        // PRESENTER
        homePresenter = new HomePresenter(this);

    }


    private void userCheck() {
        super.onStart();
        if (currentUser == null) {
            sendUserToLoginActivity();
        } else {
            verifyProfileUpdate();
        }
    }

    private void verifyProfileUpdate() {
        progressDialog.setMessage("Setting up ...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        homePresenter.verifyUsernameExists();
    }

    @Override
    public void handleNetworkRequestError(String message) {
        progressDialog.dismiss();

        Toast.makeText(MainActivity.this,
                message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGroupCreationSuccess(String groupName) {
        progressDialog.dismiss();
        Toast.makeText(MainActivity.this,
                groupName+" has been created successfully",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void handleUsernameVerificationResult(Boolean isUsernameAvailable) {
        progressDialog.dismiss();

        if (!isUsernameAvailable) {
            Toast.makeText(MainActivity.this, "Kindly Update your username", Toast.LENGTH_SHORT).show();
            sendUserToSettingsActivity();
        }
    }

    private void sendUserToSettingsActivity() {
        progressDialog.dismiss();

        Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
        settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(settingsIntent);
        finish();
    }

    private void sendUserToLoginActivity() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.option_find_friends:
                Toast.makeText(this, "Find Friends", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.option_settings:
                launchSettingsActivity();
                return true;
            case R.id.option_log_out:
                firebaseAuth.signOut();
                sendUserToLoginActivity();
                return true;
            case R.id.option_create_group:
                requestNewGroup();
                return true;
            default:
                return false;
        }
    }

    private void requestNewGroup() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        dialogBuilder.setTitle("Enter Group Name");

        final EditText groupNameInput = new EditText(MainActivity.this);
        groupNameInput.setHint("    e.g Programmers Court");

        dialogBuilder.setView(groupNameInput);
        dialogBuilder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                initiateNewGroupCreation(groupNameInput);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialogBuilder.show();

    }

    private void initiateNewGroupCreation(EditText groupNameInput) {
        progressDialog.setMessage("Creating new group...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        // create group on firebase
        String groupName = groupNameInput.getText().toString();
        if (!TextUtils.isEmpty(groupName)) {
            homePresenter.createNewGroup(groupName);
        } else {
            Toast.makeText(MainActivity.this, "Please enter a name for your group", Toast.LENGTH_SHORT).show();
        }
    }

    private void launchSettingsActivity() {
        Intent settingsActivityIntent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(settingsActivityIntent);
    }
}
