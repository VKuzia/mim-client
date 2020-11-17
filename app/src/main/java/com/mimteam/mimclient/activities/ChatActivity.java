package com.mimteam.mimclient.activities;

import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.mimteam.mimclient.models.MessageModel;
import com.mimteam.mimclient.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ChatActivity extends CustomActivity {

    private EditText inputEdit;
    private ListView messagesList;
    private Button sendButton;
    private Toolbar chatToolbar;

    private ArrayList<MessageModel> messages;
    private ArrayAdapter<MessageModel> messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupMessageList();
    }

    @Override
    protected void setToolBar() {
        setSupportActionBar(chatToolbar);
    }

    @Override
    protected void setView() {
        setContentView(R.layout.chat);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    protected void initializeUIComponents() {
        inputEdit = findViewById(R.id.inputEdit);
        messagesList = findViewById(R.id.listOfMessages);
        sendButton = findViewById(R.id.sendButton);
        chatToolbar = findViewById(R.id.toolBarChat);
    }

    @Override
    protected void attachListenersToComponents() {
        sendButton.setOnClickListener(v -> handleOnSendButtonClicked());
        chatToolbar.setNavigationOnClickListener(v -> handleOnNavigationButtonClicked());
        inputEdit.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    return handleOnKeyPressed();
                }
            }
            return false;
        });
    }

    private void setupMessageList() {
        messages = new ArrayList<>();
        messageAdapter = new ArrayAdapter<MessageModel>(this, R.layout.message_markup, messages) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.message_markup, parent, false);
                }
                setupMessageModel(convertView, messages.get(position));
                return convertView;
            }
        };
        messagesList.setAdapter(messageAdapter);
    }

    private void setupMessageModel(@NotNull View convertView, @NotNull MessageModel messageModel) {
        TextView userName = convertView.findViewById(R.id.userName);
        TextView message = convertView.findViewById(R.id.message);
        TextView time = convertView.findViewById(R.id.time);

        userName.setText(messageModel.getUserName());
        message.setText(messageModel.getMessage());
        time.setText(messageModel.getTimeString());
    }

    private void handleOnNavigationButtonClicked() {
        intent.setClass(ChatActivity.this, ChatListActivity.class);
        startActivity(intent);
    }

    private void handleOnSendButtonClicked() {
        handleMessage();
    }

    private boolean handleOnKeyPressed() {
        return handleMessage();
    }

    private boolean handleMessage() {
        if (inputEdit.getText().toString().length() <= 0) {
            return false;
        }
        messages.add(new MessageModel(getString(R.string.user_name), inputEdit.getText().toString()));
        messageAdapter.notifyDataSetChanged();
        inputEdit.getText().clear();
        return true;
    }
}
