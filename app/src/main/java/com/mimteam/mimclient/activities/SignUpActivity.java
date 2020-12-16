package com.mimteam.mimclient.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.common.base.Optional;
import com.mimteam.mimclient.App;
import com.mimteam.mimclient.MainActivity;
import com.mimteam.mimclient.R;

public class SignUpActivity extends AppCompatActivity {

    private EditText usernameEdit;
    private EditText loginEdit;
    private EditText passwordEdit;
    private Button signUpButton;
    private TextView toLoginView;

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
        toLoginView = findViewById(R.id.toLogin);
        signUpButton = findViewById(R.id.signUpButton);
    }

    private void attachListenersToComponents() {
        toLoginView.setOnClickListener(button -> finish());
        signUpButton.setOnClickListener(button -> createAccount());
    }

    private void createAccount() {
        String username = usernameEdit.getText().toString();
        if (username.length() == 0) {
            usernameEdit.setError(getString(R.string.user_name) + " " +
                    getString(R.string.sign_up_field_error));
        }
        String login = loginEdit.getText().toString();
        if (login.length() == 0) {
            loginEdit.setError(getString(R.string.login) + " " +
                    getString(R.string.sign_up_field_error));
        }
        String password = passwordEdit.getText().toString();
        if (password.length() == 0) {
            passwordEdit.setError(getString(R.string.password) + " " +
                    getString(R.string.sign_up_field_error));
        }
        if (usernameEdit.getError() != null ||
                loginEdit.getError() != null ||
                passwordEdit.getError() != null) {
            return;
        }
        Optional<String> response =
                ((App) getApplication()).getHttpWrapper().signUp(username, login, password);
        if (!response.isPresent()) {
            ((App) getApplication()).showNotification(this,
                    getString(R.string.sign_up_error), getString(R.string.sign_up_error_title));
            return;
        }
        ((App) getApplication()).showNotification(this,
                getString(R.string.sign_up_ok),
                getString(R.string.sign_up_ok_title),
                this::finish);
    }
}
