package com.mimteam.mimclient.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.common.base.Optional;
import com.mimteam.mimclient.App;
import com.mimteam.mimclient.R;

public class ChatSettingsActivity extends AppCompatActivity {

    private TextView linkView;
    private ImageView imageView;
    private Toolbar settingsToolbar;

    private ClipboardManager clipboardManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_settings);
        setSupportActionBar(settingsToolbar);

        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        initializeUIComponents();
        attachListenersToComponents();
        updateLink();
    }

    private void initializeUIComponents() {
        linkView = findViewById(R.id.linkView);
        imageView = findViewById(R.id.shareImage);
        settingsToolbar = findViewById(R.id.chatSettingsToolbar);
    }

    private void attachListenersToComponents() {
        settingsToolbar.setNavigationOnClickListener(v -> MainActivity.switchActivity(ChatActivity.class));
        imageView.setOnClickListener(v -> copyLinkToBuffer());
    }

    private void updateLink() {
        App application = (App) getApplication();
        Optional<String> key = application.getHttpWrapper().getInvitationKey(application.getOpenedChatId());
        if (key.isPresent()) {
            linkView.setText(key.get());
        } else {
            application.showNotification(this, getString(R.string.chat_link_error),
                    getString(R.string.chat_link_error_title));
        }
    }

    private void copyLinkToBuffer() {
        String text = linkView.getText().toString();
        ClipData clipData = ClipData.newPlainText("text", text);
        clipboardManager.setPrimaryClip(clipData);
    }
}
