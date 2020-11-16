package com.mimteam.mimclient.activities;

import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
    private Toolbar toolbar;
    private FloatingActionButton addChat;

    private ArrayList<ChatModel> chats;
    private ArrayAdapter<ChatModel> chatAdapter;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_chats);
        intent = new Intent();

        initializeUIComponents();
        setSupportActionBar(toolbar);
        setupChatList();
        attachListenersToComponents();
    }

    @Override
    protected void initializeUIComponents() {
        listChats = findViewById(R.id.listOfChats);
        toolbar = findViewById(R.id.toolBarChat);
        addChat = findViewById(R.id.fab);
    }

    @Override
    protected void attachListenersToComponents() {
        addChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleAddChatButtonClicked();
            }
        });
        listChats.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                handleListViewItemClicked();
            }
        });
    }

    private void setupChatList() {
        chats = new ArrayList<>();
        chatAdapter = new ArrayAdapter<ChatModel>(this, R.layout.chat_markup, chats) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater()
                            .inflate(R.layout.chat_markup, parent, false);
                }
                ChatModel chatModel = chats.get(position);
                setupChatModel(convertView, chatModel);

                return convertView;
            }
        };;
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
        int resourceID = getResources().getIdentifier(chatModel.getImage(),
                "drawable", getPackageName());
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
        chats.add(new ChatModel(new MessageModel("Name",
                "Hello! How are you? I wish you good luck"),
                "Chat " + chats.size(),
                "@drawable/hacker"));
        chatAdapter.notifyDataSetChanged();
    }
}
