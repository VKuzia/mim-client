package com.mimteam.mimclient.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.common.base.Optional;
import com.mimteam.mimclient.App;
import com.mimteam.mimclient.MainActivity;
import com.mimteam.mimclient.R;
import com.mimteam.mimclient.client.UserInfo;
import com.mimteam.mimclient.components.ui.ExtendedEditText;
import com.mimteam.mimclient.util.validors.AlphanumericValidator;
import com.mimteam.mimclient.util.validors.NonEmptyValidator;

public class SignInActivity extends AppCompatActivity {

    private ExtendedEditText loginEdit;
    private ExtendedEditText passwordEdit;
    private Button signInButton;
    private TextView toSignUpView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        initializeUIComponents();
        attachListenersToComponents();
    }

    @Override
    public void onBackPressed() {
    }

    private void initializeUIComponents() {
        loginEdit = findViewById(R.id.signInLoginEdit);
        passwordEdit = findViewById(R.id.signInPasswordEdit);
        toSignUpView = findViewById(R.id.toSignUp);
        signInButton = findViewById(R.id.signInButton);
    }

    private void attachListenersToComponents() {
        toSignUpView.setOnClickListener(button -> MainActivity.switchActivity(SignUpActivity.class));
        signInButton.setOnClickListener(button -> authorize());
    }

    private void authorize() {
        App application = (App) getApplication();
        loginEdit.validate(new AlphanumericValidator());
        passwordEdit.validate(new NonEmptyValidator());
        if (passwordEdit.getError() != null || loginEdit.getError() != null) {
            return;
        }
        String login = loginEdit.getStringValue();
        String password = passwordEdit.getStringValue();
        Optional<String> response = application.getHttpWrapper().login(login, password);
        if (!response.isPresent()) {
            application.showNotification(this,
                    getString(R.string.sign_in_error), getString(R.string.sign_in_error_title));
            return;
        }

        UserInfo userInfo = application.getUserInfo();
        userInfo.clearChats();
        userInfo.setToken(response.get());
        userInfo.setId(application.getHttpWrapper().getUserId().or(-1));
        application.connectWebSocket();
        Log.d("SIGN_IN", response.get());
        MainActivity.switchActivity(ChatListActivity.class);
    }
}
