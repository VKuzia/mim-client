package com.mimteam.mimclient.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public abstract class CustomActivity extends AppCompatActivity {

    protected Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = new Intent();

        setView();
        initializeUIComponents();
        setToolBar();
        attachListenersToComponents();
    }

    protected abstract void setToolBar();

    protected abstract void setView();

    protected abstract void initializeUIComponents();

    protected abstract void attachListenersToComponents();
}
