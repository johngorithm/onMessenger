package com.jxw.onmessenger.groupchat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.jxw.onmessenger.R;

public class GroupChatActivity extends AppCompatActivity {
    private Toolbar groupChatToolbar;
    private ImageButton sendBtn;
    private ScrollView chatScrollView;
    private TextView messageTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        initializeProperties();
    }

    private void initializeProperties() {
        groupChatToolbar = findViewById(R.id.group_chat_bar_layout);
        sendBtn = findViewById(R.id.group_chat_send_button);
        chatScrollView = findViewById(R.id.group_chat_scroll_view);
        messageTextView = findViewById(R.id.group_chat_text_display);

        setSupportActionBar(groupChatToolbar);
        if (getIntent().hasExtra("group_name")) {
            String groupName = getIntent().getStringExtra("group_name");
            getSupportActionBar().setTitle(groupName);
        } else {
            Toast.makeText(this, "Group Name Does Not Exist", Toast.LENGTH_SHORT).show();
            getSupportActionBar().setTitle(R.string.app_name);
        }
    }
}
