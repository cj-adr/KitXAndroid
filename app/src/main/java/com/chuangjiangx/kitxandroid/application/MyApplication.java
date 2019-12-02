package com.chuangjiangx.kitxandroid.application;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.chuangjiangx.core.KitX;
import com.facebook.stetho.Stetho;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        KitX.init(this);
        Stetho.initializeWithDefaults(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
