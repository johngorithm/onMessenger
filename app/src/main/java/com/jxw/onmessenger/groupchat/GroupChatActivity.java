package com.jxw.onmessenger.groupchat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.jxw.onmessenger.R;
import com.jxw.onmessenger.models.Message;


public class GroupChatActivity extends AppCompatActivity implements GroupChatView {
    private Toolbar groupChatToolbar;
    private ImageButton sendBtn;
    private EditText messageInputField;

    private RecyclerView groupChatRecyclerView;
    private GroupChatPresenter groupChatPresenter;
    private String groupId;
    private ProgressDialog progressDialog;

    private GroupChatAdapter adapter;

    private View.OnClickListener sendBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String message = messageInputField.getText().toString();
            if (!TextUtils.isEmpty(message)) {
                messageInputField.setText("");
                groupChatPresenter.sendMessage(message, groupId);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        initializeProperties();
        initiateNetworkRequest();

        sendBtn.setOnClickListener(sendBtnClickListener);
    }

    private void initiateNetworkRequest() {
        groupChatPresenter.fetchGroupChats(groupId);
    }

    private void initializeProperties() {
        groupChatToolbar = findViewById(R.id.group_chat_bar_layout);
        sendBtn = findViewById(R.id.group_chat_send_button);
        messageInputField = findViewById(R.id.group_chat_input_field);
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

        adapter = new GroupChatAdapter(this);
        groupChatRecyclerView.setAdapter(adapter);
    }

    @Override
    public void handNetworkError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayMessage(Message message) {
        SharedPreferences localStore = getSharedPreferences(getString(R.string.share_pref_key), Context.MODE_PRIVATE);
        String username = localStore.getString("username", "");
        adapter.setMessage(message);
        if ((groupChatRecyclerView.getAdapter() != null) && username.equals(message.getSender().getUsername())) {
            groupChatRecyclerView.smoothScrollToPosition(groupChatRecyclerView.getAdapter().getItemCount());
        }
    }
}
