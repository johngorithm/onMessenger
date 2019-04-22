package com.jxw.onmessenger.groupchat;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Toast;

import com.jxw.onmessenger.R;
import com.jxw.onmessenger.models.Message;

import java.util.List;

public class GroupChatActivity extends AppCompatActivity implements GroupChatView {
    private Toolbar groupChatToolbar;
    private ImageButton sendBtn;
    private ScrollView chatScrollView;

    private RecyclerView groupChatRecyclerView;
    private GroupChatPresenter groupChatPresenter;
    private String groupId;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        initializeProperties();
        initiateNetworkRequest();
    }

    private void initiateNetworkRequest() {
        progressDialog.setMessage("Fetching Chats");
        groupChatPresenter.fetchGroupChats(groupId);
    }

    private void initializeProperties() {
        groupChatToolbar = findViewById(R.id.group_chat_bar_layout);
        sendBtn = findViewById(R.id.group_chat_send_button);
        chatScrollView = findViewById(R.id.group_chat_scroll_view);
        groupChatRecyclerView = findViewById(R.id.group_chat_recycler_view);

        setSupportActionBar(groupChatToolbar);
        if (getIntent().hasExtra("group_name")) {
            String groupName = getIntent().getStringExtra("group_name");
            getSupportActionBar().setTitle(groupName);
        } else {
            Toast.makeText(this, "Group Name Does Not Exist", Toast.LENGTH_SHORT).show();
            getSupportActionBar().setTitle(R.string.app_name);
        }

        if (getIntent().hasExtra("group_id")) {
            groupId = getIntent().getStringExtra("group_id");
        } else {
            Toast.makeText(this, "Oops!... Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
        }

        // PRESENTER
        groupChatPresenter = new GroupChatPresenter(this);
        // Adapter
        LinearLayoutManager mlayoutManager = new LinearLayoutManager(this);
        groupChatRecyclerView.setLayoutManager(mlayoutManager);
        // ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void handNetworkError(String message) {
        progressDialog.dismiss();

    }

    @Override
    public void displayGroupChats(List<Message> messages) {
        progressDialog.dismiss();

        if (messages != null && !messages.isEmpty() ) {
            GroupChatAdapter adapter = new GroupChatAdapter(this, messages);
            groupChatRecyclerView.setAdapter(adapter);
        }
    }
}
