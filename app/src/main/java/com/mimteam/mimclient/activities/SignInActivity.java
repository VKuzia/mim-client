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
import com.mimteam.mimclient.components.ui.NamedEditText;
import com.mimteam.mimclient.util.validators.EditTextGroupValidator;
import com.mimteam.mimclient.util.validators.schemes.AlphanumericValidationScheme;
import com.mimteam.mimclient.util.validators.schemes.NonEmptyValidationScheme;

public class SignInActivity extends AppCompatActivity {

    private NamedEditText loginEdit;
    private NamedEditText passwordEdit;
    private Button signInButton;
    private TextView toSignUpView;
    private final EditTextGroupValidator validator = new EditTextGroupValidator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        initializeUIComponents();
        attachListenersToComponents();
        configureValidator();
    }

    private void configureValidator() {
        validator.setupEditTextValidation(loginEdit)
                .with(new NonEmptyValidationScheme(), new AlphanumericValidationScheme());
        validator.setupEditTextValidation(passwordEdit)
                .with(new NonEmptyValidationScheme());
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
        if (!validator.validate()) {
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
