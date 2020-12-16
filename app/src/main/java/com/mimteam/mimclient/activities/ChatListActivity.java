package com.mimteam.mimclient.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mimteam.mimclient.App;
import com.mimteam.mimclient.MainActivity;
import com.mimteam.mimclient.adapters.ChatAdapter;
import com.mimteam.mimclient.client.UserInfo;
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

    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_chats);

        initializeUIComponents();
        setSupportActionBar(chatListToolbar);
        attachListenersToComponents();
        setupChatList();

        userInfo = ((App) getApplication()).getUserInfo();

        ((App) getApplication()).connectWebSocket();
        userInfo.setOnChatListChanged(this::updateChatList);
    }

    private void updateChatList() {
        chats.clear();
        for (Integer chatId : userInfo.getChatIds()) {
            createChat(chatId.toString());
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        String createdChatName = intent.getStringExtra(getString(R.string.chat_name_variable));
        if (createdChatName != null) {
            createChat(createdChatName);
        }
    }

    private void initializeUIComponents() {
        chatsList = findViewById(R.id.listOfChats);
        chatListToolbar = findViewById(R.id.toolBarChat);
        addChat = findViewById(R.id.fab);
    }

    private void attachListenersToComponents() {
        addChat.setOnClickListener(view -> MainActivity.switchActivity(CreateChatActivity.class));
        chatsList.setOnItemClickListener(
                (parent, view, position, id) -> {
                    Log.i("CHAT_NAME", chats.get((int) id).getChatName());
                    ((App) getApplication()).setOpenedChatId(Integer.valueOf(chats.get((int) id).getChatName()));
                    MainActivity.switchActivity(ChatActivity.class);
                });
    }

    private void setupChatList() {
        chats = new ArrayList<>();
        chatAdapter = new ChatAdapter(this, R.layout.chat_markup, chats);
        chatsList.setAdapter(chatAdapter);
    }

    private void createChat(String chatName) {
        MessageModel messageModel = new MessageModel(getString(R.string.user_name), getString(R.string.test_message));
        chats.add(new ChatModel(messageModel, chatName));
        chatAdapter.notifyDataSetChanged();
    }
}
