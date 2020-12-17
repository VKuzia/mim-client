package com.mimteam.mimclient.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mimteam.mimclient.MainActivity;
import com.mimteam.mimclient.R;

public class ChatSettingsActivity extends AppCompatActivity {

    private TextView textView;
    private ImageView imageView;

    private ClipboardManager clipboardManager;
    private ClipData clipData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_settings);

        clipboardManager=(ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        initializeUIComponents();
        attachListenersToComponents();
    }

    private void initializeUIComponents() {
        textView = findViewById(R.id.linkView);
        imageView = findViewById(R.id.shareImage);
    }

    private void attachListenersToComponents() {
        imageView.setOnClickListener(v -> copyLinkToBuffer());
    }

    private void copyLinkToBuffer() {
        String text = textView.getText().toString();
        clipData = ClipData.newPlainText("text",text);
        clipboardManager.setPrimaryClip(clipData);
    }
}
