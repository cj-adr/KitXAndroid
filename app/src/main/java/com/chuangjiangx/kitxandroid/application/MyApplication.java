package com.chuangjiangx.kitxandroid.application;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.chuangjiangx.core.KitX;
import com.chuangjiangx.core.speak.SpeakManager;
import com.facebook.stetho.Stetho;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        KitX.init(this);
        Stetho.initializeWithDefaults(this);

        SpeakManager.INSTANCE.init("17927943", "4WZmsPVtQd9ObfZUuCEtrMGZ"
                , "6rm0zsbbfSM2OumDlWH7A8tqpNGQ00xO");
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
