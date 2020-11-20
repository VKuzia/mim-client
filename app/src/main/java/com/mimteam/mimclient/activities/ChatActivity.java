package com.mimteam.mimclient.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.mimteam.mimclient.MainActivity;
import com.mimteam.mimclient.adapters.MessageAdapter;
import com.mimteam.mimclient.models.MessageModel;
import com.mimteam.mimclient.R;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private EditText inputEdit;
    private ListView messagesList;
    private Button sendButton;
    private Toolbar chatToolbar;

    private ArrayList<MessageModel> messages;
    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);

        initializeUIComponents();
        setSupportActionBar(chatToolbar);
        attachListenersToComponents();
        setupMessageList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    private void initializeUIComponents() {
        inputEdit = findViewById(R.id.inputEdit);
        messagesList = findViewById(R.id.listOfMessages);
        sendButton = findViewById(R.id.sendButton);
        chatToolbar = findViewById(R.id.toolBarChat);
    }

    private void attachListenersToComponents() {
        sendButton.setOnClickListener(v -> sendMessage());
        chatToolbar.setNavigationOnClickListener(v -> MainActivity.switchActivity(ChatListActivity.class));
        inputEdit.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    return sendMessage();
                }
            }
            return false;
        });
    }

    private void setupMessageList() {
        messages = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, R.layout.message_markup, messages);
        messagesList.setAdapter(messageAdapter);
    }

    private boolean sendMessage() {
        if (inputEdit.getText().toString().length() <= 0) {
            return false;
        }
        messages.add(new MessageModel(getString(R.string.user_name), inputEdit.getText().toString()));
        messageAdapter.notifyDataSetChanged();
        inputEdit.getText().clear();
        return true;
    }
}
