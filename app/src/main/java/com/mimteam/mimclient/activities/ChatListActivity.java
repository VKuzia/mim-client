package com.mimteam.mimclient.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mimteam.mimclient.MainActivity;
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

    private void initializeUIComponents() {
        chatsList = findViewById(R.id.listOfChats);
        chatListToolbar = findViewById(R.id.toolBarChat);
        addChat = findViewById(R.id.fab);
    }

    private void attachListenersToComponents() {
        addChat.setOnClickListener(view -> handleAddChatButtonClicked());
        chatsList.setOnItemClickListener(
                (parent, view, position, id) -> MainActivity.switchActivity(ChatActivity.class));
    }

    private void setupChatList() {
        chats = new ArrayList<>();
        chatAdapter = new ChatAdapter(this, R.layout.chat_markup, chats);
        chatsList.setAdapter(chatAdapter);
    }

    private void handleAddChatButtonClicked() {
        createChat();
    }

    private void createChat() {
        MessageModel messageModel = new MessageModel(getString(R.string.user_name), getString(R.string.test_message));
        String chatName = String.format(getString(R.string.chat_name), chats.size());
        chats.add(new ChatModel(messageModel, chatName, "@drawable/hacker"));
        chatAdapter.notifyDataSetChanged();
    }
}
