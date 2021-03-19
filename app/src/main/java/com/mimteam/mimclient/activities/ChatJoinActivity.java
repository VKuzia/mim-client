package com.mimteam.mimclient.activities;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.common.base.Optional;
import com.mimteam.mimclient.App;
import com.mimteam.mimclient.R;
import com.mimteam.mimclient.components.ui.NamedEditText;
import com.mimteam.mimclient.models.dto.ChatDto;
import com.mimteam.mimclient.models.dto.UserDto;
import com.mimteam.mimclient.util.validators.EditTextGroupValidator;
import com.mimteam.mimclient.util.validators.schemes.AlphanumericValidationScheme;
import com.mimteam.mimclient.util.validators.schemes.NonEmptyValidationScheme;

import java.util.List;

public class ChatJoinActivity extends AppCompatActivity {

    private NamedEditText linkEdit;
    private Button joinButton;
    private final EditTextGroupValidator validator = new EditTextGroupValidator();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_join_activity);
        initializeUIComponents();
        attachListenersToComponents();
        configureValidator();
    }

    private void configureValidator() {
        validator.setupEditTextValidation(linkEdit)
                .with(new NonEmptyValidationScheme(), new AlphanumericValidationScheme());
    }

    private void initializeUIComponents() {
        linkEdit = findViewById(R.id.linkEdit);
        joinButton = findViewById(R.id.joinButton);
    }

    private void attachListenersToComponents() {
        joinButton.setOnClickListener(v -> joinChat());
    }

    private void joinChat() {
        if (!validator.validate()) {
            return;
        }
        String link = linkEdit.getStringValue();
        App application = (App) getApplication();
        Optional<ChatDto> chat = application.getHttpWrapper().joinChat(link);
        if (!chat.isPresent()) {
            application.showNotification(this, getString(R.string.join_chat_error),
                    getString(R.string.join_chat_error_title), this::finish);
        } else {
            application.getUserInfo().addChat(chat.get());
            application.getWsClient().subscribe(chat.get().getChatId());
            Optional<List<UserDto>> userList = application.getHttpWrapper().getUserList(chat.get().getChatId());
            if (userList.isPresent()) {
                application.getUserInfo().updateUsers(userList.get());
            }
            finish();
        }
    }
}
