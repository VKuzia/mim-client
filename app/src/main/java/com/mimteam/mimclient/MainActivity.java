package com.mimteam.mimclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.mimteam.mimclient.activities.ChatListActivity;
import com.mimteam.mimclient.activities.SignInActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent();
        intent.setClass(this, SignInActivity.class);
        startActivity(intent);
    }
}
