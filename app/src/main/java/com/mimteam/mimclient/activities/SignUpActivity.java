package com.mimteam.mimclient.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.common.base.Optional;
import com.mimteam.mimclient.App;
import com.mimteam.mimclient.R;
import com.mimteam.mimclient.components.ui.ExtendedEditText;
import com.mimteam.mimclient.util.validors.LoginValidator;
import com.mimteam.mimclient.util.validors.PasswordValidator;
import com.mimteam.mimclient.util.validors.UsernameValidator;

public class SignUpActivity extends AppCompatActivity {

    private ExtendedEditText usernameEdit;
    private ExtendedEditText loginEdit;
    private ExtendedEditText passwordEdit;
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
        usernameEdit.validate(new UsernameValidator());
        loginEdit.validate(new LoginValidator());
        passwordEdit.validate(new PasswordValidator());
        if (usernameEdit.getError() != null ||
                loginEdit.getError() != null ||
                passwordEdit.getError() != null) {
            return;
        }
        String username = usernameEdit.getText().toString();
        String login = loginEdit.getText().toString();
        String password = passwordEdit.getText().toString();
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
