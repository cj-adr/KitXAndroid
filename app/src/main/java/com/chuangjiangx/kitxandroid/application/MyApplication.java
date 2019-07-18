package com.chuangjiangx.kitxandroid.application;

import android.app.Application;

import com.chuangjiangx.core.KitX;
import com.facebook.stetho.Stetho;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        KitX.init(this);
        Stetho.initializeWithDefaults(this);
    }
}
