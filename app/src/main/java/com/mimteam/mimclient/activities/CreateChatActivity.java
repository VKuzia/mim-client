package com.mimteam.mimclient.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.common.base.Optional;
import com.mimteam.mimclient.App;
import com.mimteam.mimclient.MainActivity;
import com.mimteam.mimclient.R;
import com.mimteam.mimclient.components.ChatAvatar;
import com.mimteam.mimclient.components.ui.ExtendedEditText;
import com.mimteam.mimclient.models.dto.ChatDTO;
import com.mimteam.mimclient.util.validors.ChatNameValidator;

public class CreateChatActivity extends AppCompatActivity {

    private ExtendedEditText chatNameEdit;
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
                chatNameEdit.validate(new ChatNameValidator());
                if (chatNameEdit.getError() == null) {
                    chatAvatar.setChatName(chatNameEdit.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void createChat() {
        chatNameEdit.validate(new ChatNameValidator());
        if (chatNameEdit.getError() != null) {
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
        MainActivity.switchActivity(ChatListActivity.class);
    }
}
