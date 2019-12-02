package com.chuangjiangx.kitxandroid.viewnavi.pages.about

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import com.chuangjiangx.kitxandroid.R
import com.chuangjiangx.kitxandroid.viewnavi.pages.Paths
import com.chuangjiangx.viewnavi.MultiViewNavigator
import com.chuangjiangx.viewnavi.compiler.annotation.Navigator
import com.chuangjiangx.viewnavi.impl.PageView
import kotlinx.android.synthetic.main.view_page_about.view.*

@Navigator(path = Paths.PATH_ABOUT)
class AboutView(context: Context) : PageView(context) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_page_about, this, true)
        setBackgroundColor(Color.WHITE)

        btn_desc.setOnClickListener { MultiViewNavigator.jump(Paths.PATH_DESC) }
    }

}