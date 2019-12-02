package com.chuangjiangx.kitxandroid.viewnavi.pages

import android.content.Context
import android.graphics.Color
import com.chuangjiangx.viewnavi.compiler.annotation.Navigator
import com.chuangjiangx.viewnavi.impl.PageView

/**
 * 初始界面
 */
@Navigator(path = Paths.PATH_INIT)
class InitView(context: Context) : PageView(context) {

    init {
        bindContainerView(this)
        setBackgroundColor(Color.WHITE)
    }

}