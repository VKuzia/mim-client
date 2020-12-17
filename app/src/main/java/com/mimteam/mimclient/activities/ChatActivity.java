package com.mimteam.mimclient.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.common.eventbus.Subscribe;
import com.mimteam.mimclient.App;
import com.mimteam.mimclient.MainActivity;
import com.mimteam.mimclient.adapters.MessageAdapter;
import com.mimteam.mimclient.client.UserInfo;
import com.mimteam.mimclient.client.WSClient;
import com.mimteam.mimclient.models.MessageModel;
import com.mimteam.mimclient.R;
import com.mimteam.mimclient.models.dto.MessageDTO;
import com.mimteam.mimclient.models.ws.messages.TextMessage;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private EditText inputEdit;
    private ListView messagesList;
    private Button sendButton;
    private Toolbar chatToolbar;

    private ArrayList<MessageModel> messages;
    private MessageAdapter messageAdapter;

    private Integer chatId;

    private UserInfo userInfo;
    private WSClient wsClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);

        initializeUIComponents();
        setSupportActionBar(chatToolbar);
        attachListenersToComponents();
        setupMessageList();

        App application = (App) getApplication();

        application.getMessagesEventBus().register(this);
        chatId = application.getOpenedChatId();
        userInfo = application.getUserInfo();
        wsClient = application.getWsClient();

        handleOldMessages(application.getMessagesStorage().getMessagesInChat(chatId));
    }

    private void handleOldMessages(List<MessageDTO> oldMessages) {
        for (MessageDTO message : oldMessages) {
            handleReceivedMessage(message);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    private void initializeUIComponents() {
        inputEdit = findViewById(R.id.inputEdit);
        messagesList = findViewById(R.id.listOfMessages);
        sendButton = findViewById(R.id.sendButton);
        chatToolbar = findViewById(R.id.toolBarChat);
    }

    private void attachListenersToComponents() {
        sendButton.setOnClickListener(v -> sendMessage());
        chatToolbar.setNavigationOnClickListener(v -> MainActivity.switchActivity(ChatListActivity.class));
        chatToolbar.setOnMenuItemClickListener(v -> {
            MainActivity.switchActivity(ChatSettingsActivity.class);
            return true;
        });
        inputEdit.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    return sendMessage();
                }
            }
            return false;
        });
    }

    private void setupMessageList() {
        messages = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, messages);
        messagesList.setAdapter(messageAdapter);
    }

    private boolean sendMessage() {
        if (inputEdit.getText().toString().length() <= 0) {
            return false;
        }
        TextMessage textMessage = new TextMessage(userInfo.getId(), chatId, inputEdit.getText().toString());
        wsClient.sendMessage(textMessage.toDataTransferObject());
        inputEdit.getText().clear();
        return true;
    }

    @Subscribe
    public void handleReceivedMessage(@NotNull MessageDTO messageDto) {
        String name = userInfo.getUserName(messageDto.getUserId(), "");
        messageAdapter.add(new MessageModel(name,
                messageDto.getContent(),
                messageDto.getDateTime()));
    }
}
