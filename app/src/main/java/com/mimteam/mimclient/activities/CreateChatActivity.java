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
import com.mimteam.mimclient.components.AvatarView;
import com.mimteam.mimclient.components.ui.NamedEditText;
import com.mimteam.mimclient.models.dto.ChatDTO;
import com.mimteam.mimclient.util.validators.EditTextGroupValidator;
import com.mimteam.mimclient.util.validators.schemes.AlphanumericValidationScheme;
import com.mimteam.mimclient.util.validators.schemes.NonEmptyValidationScheme;

public class CreateChatActivity extends AppCompatActivity {

    private NamedEditText chatNameEdit;
    private Button createChatButton;
    private AvatarView avatarView;
    private Toolbar createChatToolbar;
    private final EditTextGroupValidator validator = new EditTextGroupValidator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_chat_activity);

        initializeUIComponents();
        setSupportActionBar(createChatToolbar);
        attachListenersToComponents();
        configureValidator();
    }

    private void configureValidator() {
        validator.setupEditTextValidation(chatNameEdit)
                .with(new NonEmptyValidationScheme(), new AlphanumericValidationScheme());
    }

    private void initializeUIComponents() {
        chatNameEdit = findViewById(R.id.createChatName);
        createChatButton = findViewById(R.id.createChatButton);
        avatarView = findViewById(R.id.createChatAvatar);
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
                if (validator.validate()) {
                    avatarView.setChatName(chatNameEdit.getStringValue());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void createChat() {
        if (!validator.validate()) {
            return;
        }
        App application = (App) getApplication();
        Optional<ChatDTO> chat = application.getHttpWrapper().createChat(chatNameEdit.getStringValue());
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
