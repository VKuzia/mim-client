package com.mimteam.mimclient.activities;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.common.base.Optional;
import com.mimteam.mimclient.App;
import com.mimteam.mimclient.MainActivity;
import com.mimteam.mimclient.R;
import com.mimteam.mimclient.adapters.UserAdapter;
import com.mimteam.mimclient.models.UserModel;
import com.mimteam.mimclient.models.dto.UserDTO;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ChatSettingsActivity extends AppCompatActivity {

    private TextView nameView;
    private TextView inviteLinkView;
    private Button inviteLinkCopyButton;
    private Toolbar settingsToolbar;
    private ListView usersListView;
    private Button leaveChatButton;

    private final ArrayList<UserModel> users = new ArrayList<>();
    private UserAdapter userAdapter;

    private ClipboardManager clipboardManager;

    private Integer chatId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_settings);
        setSupportActionBar(settingsToolbar);
        chatId = ((App) getApplication()).getOpenedChatId();

        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        initializeUIComponents();
        attachListenersToComponents();
        setupUsersView();
        updateChatName();
        updateInviteLink();
        updateUsersList();
    }

    private void initializeUIComponents() {
        nameView = findViewById(R.id.chatNameView);
        inviteLinkView = findViewById(R.id.inviteLinkView);
        inviteLinkCopyButton = findViewById(R.id.inviteLinkCopyButton);
        settingsToolbar = findViewById(R.id.settingsToolbar);
        usersListView = findViewById(R.id.usersListView);
        leaveChatButton = findViewById(R.id.leaveChatButton);
    }

    private void attachListenersToComponents() {
        settingsToolbar.setNavigationOnClickListener(v -> MainActivity.switchActivity(ChatActivity.class));
        inviteLinkCopyButton.setOnClickListener(v -> copyInviteLinkToBuffer());
        leaveChatButton.setOnClickListener(v -> leaveChatWithAlert());
    }

    private void updateChatName() {
        App application = (App) getApplication();
        String chatName = application.getUserInfo().getChatNameById(application.getOpenedChatId());
        nameView.setText(chatName);
    }

    private void setupUsersView() {
        userAdapter = new UserAdapter(this, R.layout.user_markup, users);
        usersListView.setAdapter(userAdapter);
    }

    private void updateUsersList() {
        App application = (App) getApplication();
        Optional<List<UserDTO>> userList = application.getHttpWrapper().getUserList(chatId);
        if (userList.isPresent()) {
            users.clear();
            for (UserDTO userDTO : userList.get()) {
                addUser(userDTO);
            }
        } else {
            application.showNotification(this, getString(R.string.user_list_error), getString(R.string.error));
        }
    }

    private void addUser(@NotNull UserDTO userDto) {
        users.add(new UserModel(userDto.getUserId(), userDto.getUserName()));
        userAdapter.notifyDataSetChanged();
    }

    private void updateInviteLink() {
        App application = (App) getApplication();
        Optional<String> inviteLink = application.getHttpWrapper().getInvitationLink(chatId);
        if (inviteLink.isPresent()) {
            inviteLinkView.setText(inviteLink.get());
        } else {
            application.showNotification(this, getString(R.string.chat_link_error),
                    getString(R.string.chat_link_error_title));
        }
    }

    private void leaveChatWithAlert() {
        ((App) getApplication()).showYesNoDialog(this,
                getString(R.string.leave_chat_alert),
                getString(R.string.confirmation_title),
                this::leaveChat,
                () -> {});
    }

    private void leaveChat() {
        App application = (App) getApplication();
        application.getUserInfo().removeChat(chatId);
        application.getHttpWrapper().leaveChat(chatId);
        MainActivity.switchActivity(ChatListActivity.class);
    }

    private void copyInviteLinkToBuffer() {
        String inviteLink = inviteLinkView.getText().toString();
        ClipData clipData = ClipData.newPlainText("text", inviteLink);
        clipboardManager.setPrimaryClip(clipData);
    }
}
