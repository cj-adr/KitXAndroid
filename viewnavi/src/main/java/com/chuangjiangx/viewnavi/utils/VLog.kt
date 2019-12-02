package com.chuangjiangx.viewnavi.utils

import android.util.Log

object VLog {

    private const val TAG = "ViewNavi-log"

    fun d(s: String) {
        Log.d(TAG, s)
    }

    fun e(s: String) {
        Log.e(TAG, s)
    }

    fun e(e: Throwable) {
        Log.e(TAG, "", e)
    }

}