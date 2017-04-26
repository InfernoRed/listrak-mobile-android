package com.listrak.samplemobilestore;

import android.app.Application;

import com.listrak.samplemobilestore.models.Account;

/**
 * Main class as entry point into app
 * Created by Pam on 4/26/2017.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Account.initInstance(getApplicationContext());
    }
}
