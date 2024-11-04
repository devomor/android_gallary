package com.photo.photography.collage.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.photo.photography.MyApp;


public class HelperAppConfig {
    public static final String DEFAULT_LANGUAGE = "en";
    private static final String APP_PREF_NAME = "appPref";
    private static final String LANGUAGE_KEY = "language";

    public static String getLanguage() {
        SharedPreferences pref = MyApp.getInstance().getSharedPreferences(APP_PREF_NAME,
                Context.MODE_PRIVATE);
        return pref.getString(LANGUAGE_KEY, DEFAULT_LANGUAGE);
    }

    public static void setLanguage(String lang) {
        SharedPreferences pref = MyApp.getInstance().getSharedPreferences(APP_PREF_NAME,
                Context.MODE_PRIVATE);
        pref.edit().putString(LANGUAGE_KEY, lang).commit();
    }
}
