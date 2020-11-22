package com.mimteam.mimclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.mimteam.mimclient.R;
import com.mimteam.mimclient.components.ChatAvatar;

public class CreateChatActivity extends AppCompatActivity {

    private EditText chatNameEdit;
    private Button createChatButton;
    private ChatAvatar chatAvatar;
    private Toolbar createChatToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_chat_activity);

        initializeUIComponents();
        setSupportActionBar(createChatToolbar);
        attachListenersToComponents();
    }

    private void initializeUIComponents() {
        chatNameEdit = findViewById(R.id.createChatName);
        createChatButton = findViewById(R.id.createChatButton);
        chatAvatar = findViewById(R.id.createChatAvatar);
        createChatToolbar = findViewById(R.id.toolBarCreateChat);
    }

    private void attachListenersToComponents() {
        createChatButton.setOnClickListener(view -> createChat());
        createChatToolbar.setNavigationOnClickListener(view -> openChatListActivity());
        chatNameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (chatAvatar.setChatName(chatNameEdit.getText().toString())) {
                    setChatNameError();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void openChatListActivity() {
        Intent intent = new Intent(CreateChatActivity.this, ChatListActivity.class);
        startActivity(intent);
    }

    private void setChatNameError() {
        chatNameEdit.requestFocus();
        chatNameEdit.setError("Name should contain at least one letter or number");
    }

    private void createChat() {
        if (chatNameEdit.getError() != null || chatNameEdit.getText().length() == 0) {
            setChatNameError();
            return;
        }
        Intent intent = new Intent(CreateChatActivity.this, ChatListActivity.class);
        intent.putExtra("chatName", chatNameEdit.getText().toString());
        startActivity(intent);
    }
}
