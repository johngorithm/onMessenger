package com.jxw.onmessenger.find_friends;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.hbb20.GThumb;
import com.jxw.onmessenger.R;
import com.jxw.onmessenger.models.User;
import com.jxw.onmessenger.services.FirebaseService;


public class FindFriendsActivity extends AppCompatActivity {
    private RecyclerView usersRecyclerView;
    private FirebaseRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);

        initProperties();

        Toolbar toolbar = findViewById(R.id.find_friends_bar_layout);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Find Friends");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void initProperties() {
        usersRecyclerView = findViewById(R.id.find_friends_recycler_view);
        DatabaseReference allFriendsQuery = FirebaseService.getFbRootRef()
                .child("Users");

        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(allFriendsQuery, User.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<User, UserViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull User model) {
                holder.gThumbImage.loadThumbForName(model.getDisplayPicture(), model.getUsername());

                holder.statusTv.setText(model.getStatusInfo());
                holder.usernameTv.setText(model.getUsername());
            }

            @NonNull
            @Override
            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                View itemView = inflater.inflate(R.layout.friends_item_view, viewGroup, false);
                return new UserViewHolder(itemView);
            }
        };

        usersRecyclerView.setAdapter(adapter);
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTv, statusTv;
        GThumb gThumbImage;

        UserViewHolder(@NonNull View itemView) {
            super(itemView);

            gThumbImage = itemView.findViewById(R.id.ff_gthumb);
            usernameTv = itemView.findViewById(R.id.ff_username);
            statusTv = itemView.findViewById(R.id.ff_user_status);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.stopListening();
    }
}
