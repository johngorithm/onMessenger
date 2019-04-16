package com.jxw.onmessenger.home.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jxw.onmessenger.R;
import com.jxw.onmessenger.home.adapters.TabAccessAdapter;
import com.jxw.onmessenger.login.LoginActivity;
import com.jxw.onmessenger.settings.SettingsActivity;

public class MainActivity extends AppCompatActivity {
    private Toolbar appToolbar;
    private TabLayout mainTabLayout;
    private ViewPager mainViewPager;
    private TabAccessAdapter tabAccessAdapter;

    private FirebaseUser currentUser;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference fbRootRef;
    private ProgressDialog progressDialog;
    private static final String TAG = "AUTH";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        fbRootRef = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(MainActivity.this);

        appToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(appToolbar);
        getSupportActionBar().setTitle("onMessenger");

        // View Pager
        mainViewPager = findViewById(R.id.main_view_pager);
        tabAccessAdapter = new TabAccessAdapter(getSupportFragmentManager());
        mainViewPager.setAdapter(tabAccessAdapter);

        // TabLayout
        mainTabLayout = findViewById(R.id.main_tabs);
        mainTabLayout.setupWithViewPager(mainViewPager);

        userCheck();
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

        String currentUserId = currentUser.getUid();

        fbRootRef.child("Users").child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                if (!dataSnapshot.hasChild("username")
                        && dataSnapshot.child("username").getValue() != null) {
                    Toast.makeText(MainActivity.this,
                            "Kind update your username to proceed", Toast.LENGTH_SHORT).show();
                    sendUserToSettingsActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: "+databaseError.getMessage(),
                        databaseError.toException());
            }
        });
    }

    private void sendUserToSettingsActivity() {
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
            default:
                return false;
        }
    }

    private void launchSettingsActivity() {
        Intent settingsActivityIntent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(settingsActivityIntent);
    }
}
