package com.mimteam.mimclient.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.eventbus.Subscribe;
import com.mimteam.mimclient.App;
import com.mimteam.mimclient.MainActivity;
import com.mimteam.mimclient.adapters.ChatAdapter;
import com.mimteam.mimclient.client.MessagesStorage;
import com.mimteam.mimclient.client.UserInfo;
import com.mimteam.mimclient.models.ChatModel;
import com.mimteam.mimclient.models.MessageModel;
import com.mimteam.mimclient.R;
import com.mimteam.mimclient.models.dto.ChatDto;
import com.mimteam.mimclient.models.dto.MessageDto;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

public class ChatListActivity extends AppCompatActivity {

    private ListView chatsList;
    private Toolbar chatListToolbar;
    private Button joinChatButton;
    private FloatingActionButton addChat;

    private final ArrayList<ChatModel> chats = new ArrayList<>();
    private ChatAdapter chatAdapter;

    private UserInfo userInfo;
    private MessagesStorage messagesStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_chats);

        initializeUIComponents();
        setSupportActionBar(chatListToolbar);
        attachListenersToComponents();
        setupChatList();

        App application = (App) getApplication();
        userInfo = application.getUserInfo();
        messagesStorage = application.getMessagesStorage();
        application.getMessagesEventBus().register(this);
        userInfo.setOnChatListChanged(this::updateChatList);
    }

    private void updateChatList() {
        chats.clear();
        for (ChatDto chat : userInfo.getChats()) {
            addChat(chat);
        }
        Collections.sort(chats, (chat1, chat2) -> chat2.getDateTime().compareTo(chat1.getDateTime()));
        chatAdapter.notifyDataSetChanged();
    }

    @Subscribe
    public void handleReceivedMessage(@NotNull MessageDto messageDto) {
        updateChatList();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        updateChatList();
    }

    private void initializeUIComponents() {
        chatsList = findViewById(R.id.listOfChats);
        chatListToolbar = findViewById(R.id.toolBarChat);
        joinChatButton = findViewById(R.id.joinChatButton);
        addChat = findViewById(R.id.fab);
    }

    private void attachListenersToComponents() {
        addChat.setOnClickListener(view -> MainActivity.switchActivity(CreateChatActivity.class));
        chatsList.setOnItemClickListener(
                (parent, view, position, id) -> {
                    ((App) getApplication()).setOpenedChatId(chats.get((int) id).getChatId());
                    MainActivity.switchActivity(ChatActivity.class);
                });
        joinChatButton.setOnClickListener(view -> MainActivity.switchActivity(ChatJoinActivity.class));
    }

    private void setupChatList() {
        chatAdapter = new ChatAdapter(this, R.layout.chat_markup, chats);
        chatsList.setAdapter(chatAdapter);
    }

    private void addChat(@NotNull ChatDto chat) {
        MessageDto lastMessage = messagesStorage.getLastMessageInChat(chat.getChatId());
        MessageModel messageModel = new MessageModel(getString(R.string.user_name), getString(R.string.test_message));
        if (lastMessage != null) {
            String name = userInfo.getUserName(lastMessage.getUserId(), getString(R.string.your_name));
            messageModel = new MessageModel(name,
                    lastMessage.getContent(),
                    lastMessage.getDateTime());
        }
        chats.add(new ChatModel(messageModel, chat));
        chatAdapter.notifyDataSetChanged();
    }
}
