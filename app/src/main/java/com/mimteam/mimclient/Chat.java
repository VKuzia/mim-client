package com.mimteam.mimclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Chat extends AppCompatActivity {

    private EditText inputEdit;
    private ListView listMessages;
    private Button sendButton;
    private Toolbar chatToolbar;

    private ArrayList<MessageModel> messages;
    private ArrayAdapter<MessageModel> messageAdapter;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
        intent = new Intent();

        initializeUIComponents();
        setupMessageList();
        setSupportActionBar(chatToolbar);
        setListenersToComponents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    private void initializeUIComponents() {
        inputEdit = findViewById(R.id.inputEdit);
        listMessages = findViewById(R.id.listOfMessages);
        sendButton = findViewById(R.id.sendButton);
        chatToolbar = findViewById(R.id.toolBarChat);
    }

    private void setListenersToComponents() {
        sendButton.setOnClickListener(createSendButtonClickListener());
        inputEdit.setOnKeyListener(createInputEditKeyListener());
        chatToolbar.setNavigationOnClickListener(createToolbarClickListener());
    }

    private void setupMessageList() {
        messages = new ArrayList<>();
        messageAdapter = createArrayAdapter();
        listMessages.setAdapter(messageAdapter);
    }

    private ArrayAdapter<MessageModel> createArrayAdapter() {
        return new ArrayAdapter<MessageModel>(this, R.layout.message_markup, messages) {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public View getView(int position,
                                View convertView,
                                ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater()
                            .inflate(R.layout.message_markup, parent, false);
                }
                MessageModel messageModel = messages.get(position);

                TextView userName = (TextView) convertView.findViewById(R.id.userName);
                TextView message = (TextView) convertView.findViewById(R.id.message);
                TextView time = (TextView) convertView.findViewById(R.id.time);
                initializeMessageModel(messageModel, userName, message, time);

                return convertView;
            }
        };
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initializeMessageModel(MessageModel messageModel,
                                        TextView userName, TextView message, TextView time) {
        userName.setText(messageModel.getUserName());
        message.setText(messageModel.getMessage());
        time.setText(messageModel.getTimeString());
    }

    private View.OnClickListener createSendButtonClickListener() {
        return new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Log.i("fsf", "Click!");

                if (inputEdit.getText().toString().length() > 0) {
                    handleMessage();
                }
            }
        };
    }

    private View.OnClickListener createToolbarClickListener() {
        return new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                intent.setClass(Chat.this, ChatList.class);
                startActivity(intent);
            }
        };
    }

    private View.OnKeyListener createInputEditKeyListener() {
        return new View.OnKeyListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        if (inputEdit.getText().toString().length() > 0) {
                            handleMessage();
                            return true;
                        }
                    }
                }
                return false;
            }
        };
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void handleMessage() {
        messages.add(new MessageModel("Kate",
                inputEdit.getText().toString(),
                LocalDateTime.now()));
        messageAdapter.notifyDataSetChanged();
        inputEdit.getText().clear();
    }
}
