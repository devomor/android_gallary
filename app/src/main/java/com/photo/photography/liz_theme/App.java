package com.photo.photography.liz_theme;

import android.app.Application;

import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.NoEncryption;


public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Hawk.init(getApplicationContext()).setEncryption(new NoEncryption()).build();
    }
}
