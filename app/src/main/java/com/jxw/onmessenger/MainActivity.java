package com.jxw.onmessenger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
    private Toolbar appToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(appToolbar);
        getSupportActionBar().setTitle("onMessenger");
    }
}
