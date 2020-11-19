package com.mimteam.mimclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mimteam.mimclient.R;

public class SignInActivity extends AppCompatActivity {

    private EditText loginEdit;
    private EditText passwordEdit;
    private Button signInButton;
    private Button signUpButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        initializeUIComponents();
        attachListenersToComponents();
    }

    // Сделал по аналогии с написанными активити, но отбъясните почему тут должно быть protected :(
    protected void initializeUIComponents() {
        loginEdit = findViewById(R.id.signInLoginEdit);
        passwordEdit = findViewById(R.id.signInPasswordEdit);
        signUpButton = findViewById(R.id.signUpButton);
        signInButton = findViewById(R.id.signInButton);
    }

    protected void attachListenersToComponents() {
        // так как тут просто переход к другой активити, то решил бахнуть анонимуса
//        signUpButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(SignInActivity.this, SignUpActivity.class);
//                startActivity(intent);
//            }
//        });
        signUpButton.setOnClickListener(button -> reg());
        signInButton.setOnClickListener(button -> authorization());
    }

    private void reg() {
        Intent intent = new Intent();
        intent.setClass(SignInActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    private void authorization() {
        String login = loginEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        //ToDo запрос на сервер LET ME IN!!!!
        //а пока просто идем транзитом
        Intent intent = new Intent();
        intent.setClass(SignInActivity.this, ChatListActivity.class);
        startActivity(intent);
    }
}
