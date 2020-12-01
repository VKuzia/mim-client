package com.mimteam.mimclient.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mimteam.mimclient.adapters.ChatAdapter;
import com.mimteam.mimclient.models.ChatModel;
import com.mimteam.mimclient.models.MessageModel;
import com.mimteam.mimclient.R;

import java.util.ArrayList;

public class ChatListActivity extends AppCompatActivity {

    private ListView chatsList;
    private Toolbar chatListToolbar;
    private FloatingActionButton addChat;

    private ArrayList<ChatModel> chats;
    private ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_chats);

        initializeUIComponents();
        setSupportActionBar(chatListToolbar);
        attachListenersToComponents();
        setupChatList();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        String createdChatName = intent.getStringExtra(getString(R.string.chat_name_variable));
        if (createdChatName != null) {
            createChat(createdChatName);
        }
    }

    protected void initializeUIComponents() {
        chatsList = findViewById(R.id.listOfChats);
        chatListToolbar = findViewById(R.id.toolBarChat);
        addChat = findViewById(R.id.fab);
    }

    protected void attachListenersToComponents() {
        addChat.setOnClickListener(view -> handleAddChatButtonClicked());
        chatsList.setOnItemClickListener((parent, view, position, id) -> handleListViewItemClicked());
    }

    private void setupChatList() {
        chats = new ArrayList<>();
        chatAdapter = new ChatAdapter(this, R.layout.chat_markup, chats);
        chatsList.setAdapter(chatAdapter);
    }

    private void handleAddChatButtonClicked() {
        Intent intent = new Intent(ChatListActivity.this, CreateChatActivity.class);
        startActivity(intent);
    }

    private void handleListViewItemClicked() {
        Intent intent = new Intent();
        intent.setClass(ChatListActivity.this, ChatActivity.class);
        startActivity(intent);
    }

    private void createChat(String chatName) {
        MessageModel messageModel = new MessageModel(getString(R.string.user_name), getString(R.string.test_message));
        chats.add(new ChatModel(messageModel, chatName));
        chatAdapter.notifyDataSetChanged();
    }
}
