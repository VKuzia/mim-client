package com.mimteam.mimclient.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mimteam.mimclient.R;

import java.util.ArrayList;
import java.util.List;

public class ChatAvatar extends RelativeLayout {
    private ImageView avatarImage;

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

        avatarImage = view.findViewById(R.id.avatarImage);
        avatarText = view.findViewById(R.id.avatarText);
    }

    public boolean setChatName(String chatName) {
        List<String> words = getNonEmptyWords(chatName);
        String text = "";

        if (words.size() > 0) {
            text += words.get(0).substring(0, 1).toUpperCase();
        }
        avatarText.setText(text);
        return text.isEmpty();
    }

    private List<String> getNonEmptyWords(String text) {
        String[] words = text.split("[^\\w\\d]");
        List<String> nonEmptyWords = new ArrayList<>();
        for (String word : words) {
            if (!word.isEmpty()) {
                nonEmptyWords.add(word);
            }
        }
        return nonEmptyWords;
    }
}
