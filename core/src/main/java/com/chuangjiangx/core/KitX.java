package com.chuangjiangx.core;

import android.content.Context;

public class KitX {

    public static Context mContext;

    /**
     * 在Application中初始化
     */
    public static void init(Context context) {
        mContext = context;
    }

    /**
     * 获取Context
     */
    public static Context getContext() {
        return mContext;
    }
}
