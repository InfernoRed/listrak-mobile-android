package com.listrak.mobile;

import android.content.*;

/**
 * Class for accessing shared preferences in Android
 * Created by Pam on 5/2/2017.
 */

class AndroidSharedPreferences {
    private static AndroidSharedPreferences sInstance;
    private final android.content.Context androidContext;

    private AndroidSharedPreferences(android.content.Context context) {
        androidContext = context;
    }

    protected static void initializeContext(android.content.Context context) {
        sInstance = new AndroidSharedPreferences(context);
    }

    protected static void saveSharedPreferences(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(key, value);
        editor.apply();
    }

    protected static String readSharedPreferences(String key) {
        return getSharedPreferences().getString(key, null);
    }

    private static SharedPreferences getSharedPreferences() {
        final String PREFERENCES_KEY = "LISTRAK-MOBILE-SETTINGS";

        SharedPreferences sharedPreferences = sInstance.androidContext.getSharedPreferences(PREFERENCES_KEY, android.content.Context.MODE_PRIVATE);

        return sharedPreferences;
    }
}
