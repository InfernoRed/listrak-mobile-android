package com.listrak.samplemobilestore;

import android.app.Application;
import android.content.Context;

import com.listrak.mobile.Config;
import com.listrak.mobile.Session;
import com.listrak.samplemobilestore.models.Account;

import java.io.UnsupportedEncodingException;

/**
 * Main class as entry point into app
 * Created by Pam on 4/26/2017.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Context appContext = getApplicationContext();

        // LISTRAK SDK
        // initialize the sdk with test values
        //
        Config.initialize(new Config.Builder(appContext, "123", "456")
                .setHostOverride("localhost:9000")
                .setUseHttps(false)
                .build());

        Account.initInstance(appContext);

        Account account = Account.getInstance();

        try {
            // LISTRAK SDK
            // start a session in the SDK with the current signed-in user
            // or start a new session if not signed-in
            //
            if (account.isSignedIn()) {
                Session.startWithIdentity(account.getEmail(), account.getFirstName(), account.getLastName());
            } else {
                Session.start();
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
