package com.mimteam.mimclient;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

    public static void showNotification(String message) {
//        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(appContext)
//                .setMessage(message)
//                .setTitle("App Title")
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                    }
//                });
//        dlgAlert.create().show();
    }
}
