package com.mimteam.mimclient.activities;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.common.base.Optional;
import com.mimteam.mimclient.App;
import com.mimteam.mimclient.R;
import com.mimteam.mimclient.components.ui.ExtendedEditText;
import com.mimteam.mimclient.models.dto.ChatDTO;
import com.mimteam.mimclient.models.dto.UserDTO;
import com.mimteam.mimclient.util.validors.AlphanumericValidator;

import java.util.List;

public class ChatJoinActivity extends AppCompatActivity {

    private ExtendedEditText linkEdit;
    private Button joinButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_join_activity);
        initializeUIComponents();
        attachListenersToComponents();
    }

    private void initializeUIComponents() {
        linkEdit = findViewById(R.id.linkEdit);
        joinButton = findViewById(R.id.joinButton);
    }

    private void attachListenersToComponents() {
        joinButton.setOnClickListener(v -> joinChat());
    }

    private void joinChat() {
        linkEdit.validate(new AlphanumericValidator());
        if (linkEdit.getError() != null) {
            return;
        }
        String link = linkEdit.getStringValue();
        App application = (App) getApplication();
        Optional<ChatDTO> chat = application.getHttpWrapper().joinChat(link);
        if (!chat.isPresent()) {
            application.showNotification(this, getString(R.string.join_chat_error),
                    getString(R.string.join_chat_error_title), this::finish);
        } else {
            application.getUserInfo().addChat(chat.get());
            application.getWsClient().subscribe(chat.get().getChatId());
            Optional<List<UserDTO>> userList = application.getHttpWrapper().getUserList(chat.get().getChatId());
            if (userList.isPresent()) {
                application.getUserInfo().updateUsers(userList.get());
            }
            finish();
        }
    }
}
