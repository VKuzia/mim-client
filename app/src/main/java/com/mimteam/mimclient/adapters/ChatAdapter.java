package com.mimteam.mimclient.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.mimteam.mimclient.R;
import com.mimteam.mimclient.models.ChatModel;

import java.util.List;

public class ChatAdapter extends ArrayAdapter<ChatModel> {

    public ChatAdapter(@NonNull Context context, int resource, @NonNull List<ChatModel> chats) {
        super(context, resource, chats);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.chat_markup, parent, false);
        }
        setupChatModel(convertView, getItem(position));
        return convertView;
    }

    private void setupChatModel(View convertView, ChatModel chatModel) {
        TextView chatName = convertView.findViewById(R.id.chatName);
        TextView userName = convertView.findViewById(R.id.userName);
        TextView message = convertView.findViewById(R.id.message);
        TextView time = convertView.findViewById(R.id.time);
        ImageView avatar = convertView.findViewById(R.id.userAvatar);

        chatName.setText(chatModel.getChatName());
        userName.setText(chatModel.getUserName());
        message.setText(chatModel.getMessage());
        time.setText(chatModel.getTimeString());
        int resourceID = getContext().getResources().getIdentifier(chatModel.getImage(),
                "drawable", getContext().getPackageName());
        avatar.setImageResource(resourceID);
    }
}