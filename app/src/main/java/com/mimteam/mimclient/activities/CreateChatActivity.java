package com.mimteam.mimclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.common.base.Optional;
import com.mimteam.mimclient.App;
import com.mimteam.mimclient.MainActivity;
import com.mimteam.mimclient.R;
import com.mimteam.mimclient.components.ChatAvatar;
import com.mimteam.mimclient.models.dto.ChatDTO;

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
                    chatNameEdit.setError(getString(R.string.chat_name_error));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void createChat() {
        if (chatNameEdit.getError() != null || chatNameEdit.getText().length() == 0) {
            chatNameEdit.setError(getString(R.string.chat_name_error));
            return;
        }
        App application = (App) getApplication();
        Optional<ChatDTO> chat = application.getHttpWrapper().createChat(chatNameEdit.getText().toString());
        if (chat.isPresent()) {
            application.getUserInfo().addChat(chat.get());
            application.getWsClient().subscribe(chat.get().getChatId());
        }
        MainActivity.switchActivity(ChatListActivity.class);
    }

    private void openChatListActivity() {
        Intent intent = new Intent(CreateChatActivity.this, ChatListActivity.class);
        startActivity(intent);
    }
}
