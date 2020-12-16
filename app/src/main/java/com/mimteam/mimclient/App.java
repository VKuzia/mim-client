package com.mimteam.mimclient;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;

import com.mimteam.mimclient.client.HTTPClient;
import com.mimteam.mimclient.client.HTTPWrapper;
import com.mimteam.mimclient.client.UserInfo;
import com.mimteam.mimclient.client.WSClient;

public class App extends Application {
    private WSClient wsClient;
    private HTTPWrapper httpWrapper;
    private UserInfo userInfo;

    @Override
    public void onCreate() {
        super.onCreate();
        userInfo = new UserInfo(-1);
        wsClient = new WSClient(userInfo);
        httpWrapper = new HTTPWrapper(new HTTPClient(userInfo, getString(R.string.local_http_url)));
    }

    public void showNotification(Context context, String message, String title) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setTitle(title)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create().show();
    }

    public WSClient getWsClient() {
        return wsClient;
    }

    public HTTPWrapper getHttpWrapper() {
        return httpWrapper;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }
}
