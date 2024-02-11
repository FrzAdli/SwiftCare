package com.example.swiftcare.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

public class FormPreferenceManager {
    private final SharedPreferences sharedPreferences;

    public FormPreferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences(Constants.KEY_PREFERENCE_FORM, Context.MODE_PRIVATE);
    }

    public void putBoolean(String key, Boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public Boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, null);
    }

    public void putByteArray(String key, byte[] value) {
        String encodedValue = Base64.encodeToString(value, Base64.DEFAULT);
        putString(key, encodedValue);
    }

    public byte[] getByteArray(String key) {
        String encodedValue = getString(key);
        if (encodedValue != null) {
            return Base64.decode(encodedValue, Base64.DEFAULT);
        } else {
            return null;
        }
    }

    public void clear() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
