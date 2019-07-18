package com.chuangjiangx.print;

import android.util.Log;

public class PrintLogUtils {

    private static final String TAG = "PrintLogUtils";

    public static void e(Throwable e, String s) {
        Log.e(TAG, s, e);
    }

    public static void d(String s) {
        Log.d(TAG, s);
    }
}
