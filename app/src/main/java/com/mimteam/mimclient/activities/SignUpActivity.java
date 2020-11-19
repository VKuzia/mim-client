package com.mimteam.mimclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mimteam.mimclient.R;

public class SignUpActivity extends AppCompatActivity {

    private EditText usernameEdit;
    private EditText loginEdit;
    private EditText passwordEdit;
    private Button createAccountButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        initializeUIComponents();
        attachListenersToComponents();
    }

    protected void initializeUIComponents() {
        usernameEdit = findViewById(R.id.signUpUsernameEdit);
        loginEdit = findViewById(R.id.signUpLoginEdit);
        passwordEdit = findViewById(R.id.signUpPasswordEdit);
        createAccountButton = findViewById(R.id.signUpCreateAccount);
    }

    // Так как тут идет добавление слушателя только для одной кнопки,
    // может стоит это в инициализацию пихнуть?
    protected void attachListenersToComponents() {
       createAccountButton.setOnClickListener(button -> createAccout());
    }

    private void createAccout() {
        String username = usernameEdit.getText().toString();
        String login = loginEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        //ToDo создание нового пользователя
        Intent intent = new Intent();
        intent.setClass(SignUpActivity.this, ChatListActivity.class);
        startActivity(intent);
    }
}
