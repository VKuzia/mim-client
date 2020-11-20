package com.mimteam.mimclient.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.mimteam.mimclient.MainActivity;
import com.mimteam.mimclient.R;

public class SignUpActivity extends AppCompatActivity {

    private EditText usernameEdit;
    private EditText loginEdit;
    private EditText passwordEdit;
    private Button signUpButton;
    private Button signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        initializeUIComponents();
        attachListenersToComponents();
    }

    private void initializeUIComponents() {
        usernameEdit = findViewById(R.id.signUpUsernameEdit);
        loginEdit = findViewById(R.id.signUpLoginEdit);
        passwordEdit = findViewById(R.id.signUpPasswordEdit);
        signInButton = findViewById(R.id.toSignInButton);
        signUpButton = findViewById(R.id.signUpButton);
    }

    private void attachListenersToComponents() {
        signInButton.setOnClickListener(button -> finish());
        signUpButton.setOnClickListener(button -> createAccount());
    }

    private void createAccount() {
        String username = usernameEdit.getText().toString();
        String login = loginEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        MainActivity.switchActivity(ChatListActivity.class);
    }
}
