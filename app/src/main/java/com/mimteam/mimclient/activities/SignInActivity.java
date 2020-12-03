package com.mimteam.mimclient.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mimteam.mimclient.MainActivity;
import com.mimteam.mimclient.R;

public class SignInActivity extends AppCompatActivity {

    private EditText loginEdit;
    private EditText passwordEdit;
    private Button signInButton;
    private TextView toSignUpView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        initializeUIComponents();
        attachListenersToComponents();
    }

    private void initializeUIComponents() {
        loginEdit = findViewById(R.id.signInLoginEdit);
        passwordEdit = findViewById(R.id.signInPasswordEdit);
        toSignUpView = findViewById(R.id.toSignUp);
        signInButton = findViewById(R.id.signInButton);
    }

    private void attachListenersToComponents() {
        toSignUpView.setOnClickListener(button -> MainActivity.switchActivity(SignUpActivity.class));
        signInButton.setOnClickListener(button -> authorization());
    }

    private void authorization() {
        String login = loginEdit.getText().toString();
        if (login.length() == 0) {
            loginEdit.setError(getString(R.string.sign_in_error) + " " +
                    getString(R.string.login).toLowerCase());
        }
        String password = passwordEdit.getText().toString();
        if (password.length() == 0) {
            passwordEdit.setError(getString(R.string.sign_in_error) + " " +
                    getString(R.string.password).toLowerCase());
        }
        if (passwordEdit.getError() != null || loginEdit.getError() != null) {
            return;
        }
        MainActivity.switchActivity(ChatListActivity.class);
    }
}
