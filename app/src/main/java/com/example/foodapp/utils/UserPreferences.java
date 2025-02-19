package com.example.foodapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreferences {
    private static final String PREF_NAME = "UserPrefs";
    private static final String KEY_IS_LOGGED_IN = "IS_LOGGED_IN";
    private static final String KEY_USERNAME = "USERNAME";
    private static final String KEY_EMAIL = "EMAIL";
    private static final String KEY_IS_GUEST = "IS_GUEST";
    private static final String KEY_LAST_CLEANUP_DATE = "LAST_CLEANUP_DATE";


    private final SharedPreferences sharedPreferences;

    public UserPreferences(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveUserLogin(String username, String email) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putBoolean(KEY_IS_GUEST, false);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }

    public void saveGuestMode() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_IS_LOGGED_IN, false);
        editor.putBoolean(KEY_IS_GUEST, true);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public boolean isGuest() {
        return sharedPreferences.getBoolean(KEY_IS_GUEST, false);
    }

    public String getUsername() {
        return sharedPreferences.getString(KEY_USERNAME, "Guest");
    }

    public String getEmail() {
        return sharedPreferences.getString(KEY_EMAIL, "Not Available");
    }

    public void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public void setLastCleanupDate(String date) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_LAST_CLEANUP_DATE, date);
        editor.apply();
    }

    public String getLastCleanupDate() {
        return sharedPreferences.getString(KEY_LAST_CLEANUP_DATE, "");
    }
}
