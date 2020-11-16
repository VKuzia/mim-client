package com.mimteam.mimclient.activities;

import androidx.appcompat.app.AppCompatActivity;


public abstract class CustomActivity extends AppCompatActivity {

    protected abstract void initializeUIComponents();

    protected abstract void attachListenersToComponents();
}
