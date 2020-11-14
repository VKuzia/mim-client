package com.mimteam.mimclient;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class ChatList extends AppCompatActivity {

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
        setListenersToComponents();
    }

    private void initializeUIComponents() {
        listChats = findViewById(R.id.listOfChats);
        toolbar = findViewById(R.id.toolBarChat);
        addChat = findViewById(R.id.fab);
    }

    private void setListenersToComponents() {
        addChat.setOnClickListener(createUserChatClickListener());
        listChats.setOnItemClickListener(createListChatItemCLickListener());
    }

    private void setupChatList() {
        chats = new ArrayList<ChatModel>();
        chatAdapter = createArrayAdapter();
        listChats.setAdapter(chatAdapter);
    }

    private ArrayAdapter<ChatModel> createArrayAdapter() {
        return new ArrayAdapter<ChatModel>(this, R.layout.chat_markup, chats) {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater()
                            .inflate(R.layout.chat_markup, parent, false);
                }
                ChatModel chatModel = chats.get(position);

                TextView chatName = (TextView) convertView.findViewById(R.id.chatName);
                TextView message = (TextView) convertView.findViewById(R.id.message);
                TextView time = (TextView) convertView.findViewById(R.id.time);
                ImageView avatar = (ImageView) convertView.findViewById(R.id.userAvatar);

                initializeChatModel(chatModel, chatName, message, time, avatar);
                return convertView;
            }
        };
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initializeChatModel(ChatModel chatModel,
                                     TextView chatName,
                                     TextView message,
                                     TextView time,
                                     ImageView avatar) {
        chatName.setText(chatModel.getChatName());
        message.setText(chatModel.getMessage());
        time.setText(chatModel.getTimeString());
        int resourceID = getResources().getIdentifier(chatModel.getImage(), "drawable", getPackageName());
        avatar.setImageResource(resourceID);
    }

    private View.OnClickListener createUserChatClickListener() {
        return new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                handleChat();
            }
        };
    }

    private AdapterView.OnItemClickListener createListChatItemCLickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent.setClass(ChatList.this, Chat.class);
                startActivity(intent);
            }
        };
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void handleChat() {
        chats.add(new ChatModel(new MessageModel("Name",
                "Hello! How are you? I wish you good luck", LocalDateTime.now()),
                "Chat " + chats.size(),
                "@drawable/hacker"));
        chatAdapter.notifyDataSetChanged();
    }
}
