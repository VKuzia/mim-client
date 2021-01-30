package com.mimteam.mimclient.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mimteam.mimclient.R;

import org.jetbrains.annotations.NotNull;

public class ChatAvatar extends RelativeLayout {
    private TextView avatarText;

    public ChatAvatar(Context context) {
        super(context);
        initializeComponents();
    }

    public ChatAvatar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeComponents();
    }

    private void initializeComponents() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.chat_avatar_component, this);
        avatarText = view.findViewById(R.id.avatarText);
    }

    public void setChatName(String chatName) {
        String text = getFirstNonEmptyWord(chatName);
        text = text.substring(0, 1).toUpperCase();
        avatarText.setText(text);
    }

    private @NotNull String getFirstNonEmptyWord(@NotNull String text) {
        String[] words = text.split("[^\\w\\d]");
        for (String word : words) {
            if (!word.isEmpty()) {
                return word;
            }
        }
        return "";
    }
}
