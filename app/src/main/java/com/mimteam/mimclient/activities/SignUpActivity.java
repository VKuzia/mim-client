package com.mimteam.mimclient.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.common.base.Optional;
import com.mimteam.mimclient.App;
import com.mimteam.mimclient.R;
import com.mimteam.mimclient.components.ui.NamedEditText;
import com.mimteam.mimclient.util.validators.EditTextGroupValidator;
import com.mimteam.mimclient.util.validators.schemes.AlphanumericValidationScheme;
import com.mimteam.mimclient.util.validators.schemes.NonEmptyValidationScheme;

public class SignUpActivity extends AppCompatActivity {

    private NamedEditText usernameEdit;
    private NamedEditText loginEdit;
    private NamedEditText passwordEdit;
    private Button signUpButton;
    private TextView toLoginView;
    private final EditTextGroupValidator validator = new EditTextGroupValidator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        initializeUIComponents();
        attachListenersToComponents();
        configureValidator();
    }

    private void configureValidator() {
        validator.setupEditTextValidation(usernameEdit)
                .with(new NonEmptyValidationScheme(), new AlphanumericValidationScheme());
        validator.setupEditTextValidation(loginEdit)
                .with(new NonEmptyValidationScheme(), new AlphanumericValidationScheme());
        validator.setupEditTextValidation(passwordEdit)
                .with(new NonEmptyValidationScheme());
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
        if (!validator.validate()) {
            return;
        }
        String username = usernameEdit.getStringValue();
        String login = loginEdit.getStringValue();
        String password = passwordEdit.getStringValue();
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
