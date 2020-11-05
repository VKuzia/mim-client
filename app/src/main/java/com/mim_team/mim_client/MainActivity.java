package com.mim_team.mim_client;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Build;
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

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        EditText editText = findViewById(R.id.inputEdit);
        ListView listView = findViewById(R.id.listOfMessages);
        Button sendButton = findViewById(R.id.sendButton);

        ArrayList<MessageModel> messages = new ArrayList<>();

        ArrayAdapter adapter = new ArrayAdapter<MessageModel>(this,
                R.layout.message_markup, messages) {
            @Override
            public View getView(int position,
                                View convertView,
                                ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater()
                            .inflate(R.layout.message_markup, parent, false);
                }
                MessageModel messageView = messages.get(position);
                TextView userName = (TextView) convertView.findViewById(R.id.userName);
                TextView message = (TextView) convertView.findViewById(R.id.message);
                TextView time = (TextView) convertView.findViewById(R.id.time);
                userName.setText(messageView.userName);
                message.setText(messageView.message);
                time.setText(messageView.time);
                return convertView;
            }
        };

        listView.setAdapter(adapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().length() > 0) {
                    handleMessage(messages, adapter, editText);
                }
            }
        });

        editText.setOnKeyListener(new View.OnKeyListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        if (editText.getText().toString().length() > 0) {
                            handleMessage(messages, adapter, editText);
                            return true;
                        }
                    }
                }
                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void handleMessage(ArrayList<MessageModel> messages, ArrayAdapter adapter,
                               EditText editText) {

        LocalTime time = LocalTime.now();

        System.out.printf("Hour: %s%n", time.getHour());
        System.out.printf("Minute: %s%n", time.getMinute());

        messages.add(new MessageModel("Kate",
                editText.getText().toString(),
                time.getHour() + ":" + time.getMinute()));
        adapter.notifyDataSetChanged();
        editText.getText().clear();
    }
}