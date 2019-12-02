package com.chuangjiangx.kitxandroid.viewnavi.pages.main

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import com.chuangjiangx.kitxandroid.R
import com.chuangjiangx.kitxandroid.viewnavi.pages.Paths
import com.chuangjiangx.viewnavi.compiler.annotation.Navigator
import com.chuangjiangx.viewnavi.impl.PageView

@Navigator(path = Paths.PATH_MAIN02)
class Main02View(context: Context) : PageView(context) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_page_main02, this, true)
        setBackgroundColor(Color.WHITE)
    }
}