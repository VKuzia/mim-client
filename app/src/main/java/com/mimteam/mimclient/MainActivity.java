package com.mimteam.mimclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.mimteam.mimclient.activities.SignInActivity;

public class MainActivity extends AppCompatActivity {

    private static Context appContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appContext = this;

        MainActivity.switchActivity(SignInActivity.class);
    }

    public static void switchActivity(Class<?> activity) {
        Intent intent = new Intent(appContext, activity);
        appContext.startActivity(intent);
    }
}
