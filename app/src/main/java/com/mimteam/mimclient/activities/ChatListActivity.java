package com.mimteam.mimclient.activities;

import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mimteam.mimclient.models.ChatModel;
import com.mimteam.mimclient.models.MessageModel;
import com.mimteam.mimclient.R;

import java.util.ArrayList;

public class ChatListActivity extends CustomActivity {

    private ListView listChats;
    private Toolbar chatListToolbar;
    private FloatingActionButton addChat;

    private ArrayList<ChatModel> chats;
    private ArrayAdapter<ChatModel> chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupChatList();
    }

    @Override
    protected void setToolBar() {
        setSupportActionBar(chatListToolbar);
    }

    @Override
    protected void setView() {
        setContentView(R.layout.list_chats);
    }

    @Override
    protected void initializeUIComponents() {
        listChats = findViewById(R.id.listOfChats);
        chatListToolbar = findViewById(R.id.toolBarChat);
        addChat = findViewById(R.id.fab);
    }

    @Override
    protected void attachListenersToComponents() {
        addChat.setOnClickListener(view -> handleAddChatButtonClicked());
        listChats.setOnItemClickListener((parent, view, position, id) -> handleListViewItemClicked());
    }

    private void setupChatList() {
        chats = new ArrayList<>();
        chatAdapter = new ArrayAdapter<ChatModel>(this, R.layout.chat_markup, chats) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.chat_markup, parent, false);
                }
                setupChatModel(convertView, chats.get(position));
                return convertView;
            }
        };
        listChats.setAdapter(chatAdapter);
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
        int resourceID = getResources().getIdentifier(chatModel.getImage(), "drawable", getPackageName());
        avatar.setImageResource(resourceID);
    }

    private void handleAddChatButtonClicked() {
        handleChat();
    }

    private void handleListViewItemClicked() {
        intent.setClass(ChatListActivity.this, ChatActivity.class);
        startActivity(intent);
    }

    private void handleChat() {
        MessageModel messageModel = new MessageModel(getString(R.string.user_name), getString(R.string.test_message));

        String chatName = String.format(getString(R.string.chat_name), chats.size());
        chats.add(new ChatModel(messageModel, chatName, "@drawable/hacker"));
        chatAdapter.notifyDataSetChanged();
    }
}
