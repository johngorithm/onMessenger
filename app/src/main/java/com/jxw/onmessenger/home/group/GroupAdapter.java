package com.jxw.onmessenger.home.group;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jxw.onmessenger.R;
import com.jxw.onmessenger.groupchat.GroupChatActivity;
import com.jxw.onmessenger.models.Group;

import java.util.List;

public class GroupAdapter  extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {
    private Context context;
    private List<Group> groups;

    public GroupAdapter(Context context, List<Group> groups) {
        this.context = context;
        this.groups = groups;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.list_item_groups, parent, false);
        return new GroupViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder groupViewHolder, int position) {
        Group group = groups.get(position);
        String groupId = group.getUid();
        String groupName = group.getTitle();

        groupViewHolder.groupName.setText(groupName);
        groupViewHolder.groupItemCard.setOnClickListener( view -> {

                Toast.makeText(context, groupId+" is clicked", Toast.LENGTH_SHORT).show();
                Intent groupActivityIntent = new Intent(context, GroupChatActivity.class);
            groupActivityIntent.putExtra("group_id", groupId);
            groupActivityIntent.putExtra("group_name", groupName);
                context.startActivity(groupActivityIntent);
            });
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    class GroupViewHolder extends RecyclerView.ViewHolder {
        CardView groupItemCard;
        TextView groupName;

        GroupViewHolder(@NonNull View itemView) {
            super(itemView);

            groupItemCard = itemView.findViewById(R.id.list_item_group_card);
            groupName = itemView.findViewById(R.id.list_item_group_name);
        }
    }
}
