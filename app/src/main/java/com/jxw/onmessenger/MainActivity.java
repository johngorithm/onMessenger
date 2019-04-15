package com.jxw.onmessenger;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jxw.onmessenger.adapters.TabAccessAdapter;
import com.jxw.onmessenger.login.LoginActivity;

public class MainActivity extends AppCompatActivity {
    private Toolbar appToolbar;
    private TabLayout mainTabLayout;
    private ViewPager mainViewPager;
    private TabAccessAdapter tabAccessAdapter;

    private FirebaseUser currentUser;
    private static final String TAG = "AUTH";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

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
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (currentUser == null) {
            sendUserToLoginActivity();
        } else {
            Log.d(TAG, "onStart: "+currentUser.getEmail());
        }
    }

    private void sendUserToLoginActivity() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginIntent);
    }
}
