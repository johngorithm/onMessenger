package com.jxw.onmessenger.groupchat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jxw.onmessenger.R;
import com.jxw.onmessenger.models.Message;
import com.jxw.onmessenger.models.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GroupChatAdapter  extends RecyclerView.Adapter<GroupChatAdapter.ChatViewHolder> {
    private Context context;
    private List<Message> messages;

    public GroupChatAdapter(Context context, List<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.list_item_messages, parent, false);
        return new ChatViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Message chat = messages.get(position);
        String message = chat.getMessage();
        long timeStamp = chat.getCreatedAt();
        User sender = chat.getSender();
        String senderUsername = sender.getUsername();

        Date chatTime = new Date(timeStamp);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");

        holder.chatMessage.setText(message);
        holder.chatSender.setText(senderUsername);
        holder.chatTimeStamp.setText(sdf.format(chatTime));

        /**
         * TODO: implement swipe-to-reply functionality on the card view
         */

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class ChatViewHolder extends RecyclerView.ViewHolder {
        CardView chatMessageCard;
        TextView chatMessage;
        TextView chatSender;
        TextView chatTimeStamp;

        ChatViewHolder(@NonNull View itemView) {
            super(itemView);

            chatMessageCard = itemView.findViewById(R.id.chat_card_view);
            chatMessage = itemView.findViewById(R.id.chat_message);
            chatSender = itemView.findViewById(R.id.chat_sender);
            chatTimeStamp = itemView.findViewById(R.id.chat_time_stamp);
        }
    }
}
