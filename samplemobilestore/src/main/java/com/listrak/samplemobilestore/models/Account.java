package com.listrak.samplemobilestore.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ToggleButton;

import com.listrak.mobile.Session;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Account model
 * Created by Pam on 4/26/2017.
 */

public class Account {

    public interface IAccountListener {
        void onAccountChanged();
    }

    private static Account instance;

    private Account(Context context){
        mContext = context;
        readSharedPreferences();
    }

    public static Account getInstance() {
        return instance;
    }

    public static void initInstance(Context context) {
        instance = new Account(context);
    }

    private final Context mContext;
    private List<Account.IAccountListener> mAccountListeners = new ArrayList<>();
    private String mEmail;
    private String mFirstName;
    private String mLastName;
    private static final String PREFS_EMAIL = "email";
    private static final String PREFS_FIRST_NAME = "firstName";
    private static final String PREFS_LAST_NAME = "lastName";

    public String getEmail() {
        return mEmail;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public boolean isSignedIn() {
        return mEmail != null && !mEmail.isEmpty();
    }

    public boolean signIn(Context context, String email, final String firstName, final String lastName, boolean subscribe) {
        if (email == null || email.isEmpty() ||
                firstName == null || firstName.isEmpty() ||
                lastName == null || lastName.isEmpty()) {
            return false;
        }

        mEmail = email;
        mFirstName = firstName;
        mLastName = lastName;

        saveSharedPreferences();

        try {
            // LISTRAK SDK
            // when a user signs-in set the session identity
            // (a session should have been started when app started)
            //
            Session.setIdentity(email, firstName, lastName);


            if (subscribe)
            {
                // LISTRAK SDK
                // a user can be subscribed to a Listrak mailing list
                // This can be done anytime after a session has been identified
                //
                final Map<String, String> subscribeMeta = new HashMap<String, String>() {{ put("fname", firstName); put("lname", lastName); }};
                Session.subscribe("SAMPLEAPP", subscribeMeta);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return true;
    }

    public void signOut(Context context) {
        mEmail = null;
        mFirstName = null;
        mLastName = null;

        saveSharedPreferences();

        try {
            // LISTRAK SDK
            // always need a session
            // when user signs-out start a new session
            //
            Session.start();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
    
    public void notifyAccountChanged() {
        for (Account.IAccountListener listener : mAccountListeners) {
            listener.onAccountChanged();
        }
    }

    public void addAccountListener(Account.IAccountListener listener) {
        mAccountListeners.add(listener);
    }

    public void removeAccountListener(Account.IAccountListener listener) {
        mAccountListeners.remove(listener);
    }
    
    private void saveSharedPreferences() {
        notifyAccountChanged();

        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(PREFS_EMAIL, mEmail);
        editor.putString(PREFS_FIRST_NAME, mFirstName);
        editor.putString(PREFS_LAST_NAME, mLastName);
        editor.apply();
    }

    private void readSharedPreferences() {
        mEmail = getSharedPreferences().getString(PREFS_EMAIL, null);
        mFirstName = getSharedPreferences().getString(PREFS_FIRST_NAME, null);
        mLastName = getSharedPreferences().getString(PREFS_LAST_NAME, null);
    }

    private SharedPreferences getSharedPreferences() {
        final String PREFERENCES_KEY = "settings";

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);

        return sharedPreferences;
    }
}
