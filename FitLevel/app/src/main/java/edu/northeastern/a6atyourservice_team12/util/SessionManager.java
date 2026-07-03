// SharedPreferences usage based on:
// https://developer.android.com/training/data-storage/shared-preferences

package edu.northeastern.a6atyourservice_team12.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "sticker_app_prefs";
    private static final String KEY_USERNAME = "logged_in_username";

    private final SharedPreferences prefs;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveUsername(String username) {
        prefs.edit().putString(KEY_USERNAME, username).apply();
    }

    public String getUsername() {
        return prefs.getString(KEY_USERNAME, null);
    }

    public void clearSession() {
        prefs.edit().remove(KEY_USERNAME).apply();
    }

    public boolean isLoggedIn() {
        return getUsername() != null;
    }
}