package com.mimteam.mimclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mimteam.mimclient.MainActivity;
import com.mimteam.mimclient.R;

public class SignInActivity extends AppCompatActivity {

    private EditText loginEdit;
    private EditText passwordEdit;
    private Button signInButton;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        initializeUIComponents();
        attachListenersToComponents();
        return;
    }

    private void initializeUIComponents() {
        loginEdit = findViewById(R.id.signInLoginEdit);
        passwordEdit = findViewById(R.id.signInPasswordEdit);
        signUpButton = findViewById(R.id.toSignUpButton);
        signInButton = findViewById(R.id.signInButton);
    }

    private void attachListenersToComponents() {
        signUpButton.setOnClickListener(button -> MainActivity.switchActivity(SignUpActivity.class));
        signInButton.setOnClickListener(button -> authorization());
    }

    private void authorization() {
        String login = loginEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        MainActivity.switchActivity(ChatListActivity.class);
    }
}
