package com.photo.photography.util.helper;

/* this class is for save or retrieving data in/from shared preference */

import android.content.Context;
import android.content.SharedPreferences;

import com.photo.photography.MyApp;


public class PreferencesHelper {

    public static PreferencesHelper instance;

    private final SharedPreferences settings;

    private final SharedPreferences.Editor editor;

    private PreferencesHelper() {

        settings = MyApp.getInstance().getSharedPreferences("GalleryApp", 0);
        editor = settings.edit();
    }

    public static PreferencesHelper getInstance() {

        if (instance == null)
            instance = new PreferencesHelper();
        return instance;
    }

    public static PreferencesHelper getInstance(Context context) {
        if (instance == null)
            instance = new PreferencesHelper();
        return instance;
    }

    public String getString(String key, String defValue) {

        return settings.getString(key, defValue);
    }

    public PreferencesHelper setString(String key, String value) {

        editor.putString(key, value);
        editor.commit();

        return this;
    }

    public PreferencesHelper setStatus(String key, boolean value) {

        editor.putBoolean(key, value);
        editor.commit();

        return this;
    }

    public int getInt(String key, int defValue) {

        return settings.getInt(key, defValue);
    }

    public PreferencesHelper setInt(String key, int value) {

        editor.putInt(key, value);
        editor.commit();

        return this;
    }

    public boolean getBoolean(String key, boolean defValue) {

        return settings.getBoolean(key, defValue);
    }

    public PreferencesHelper setBoolean(String key, boolean value) {

        editor.putBoolean(key, value);
        editor.commit();

        return this;
    }

    public PreferencesHelper setLong(String key, long value) {

        editor.putLong(key, value);
        editor.commit();

        return this;
    }

    public long getLong(String key, long defValue) {

        return settings.getLong(key, defValue);
    }

    public void clearData() {

        editor.clear();
        editor.commit();
    }
}
