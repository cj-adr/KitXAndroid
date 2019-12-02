package com.chuangjiangx.kitxandroid.viewnavi.pages.main

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import com.chuangjiangx.kitxandroid.R
import com.chuangjiangx.kitxandroid.viewnavi.DataParam
import com.chuangjiangx.kitxandroid.viewnavi.pages.Paths
import com.chuangjiangx.viewnavi.MultiViewNavigator
import com.chuangjiangx.viewnavi.compiler.annotation.Navigator
import com.chuangjiangx.viewnavi.impl.PageView
import com.chuangjiangx.viewnavi.utils.VLog
import kotlinx.android.synthetic.main.view_page_main.view.*

@Navigator(path = Paths.PATH_MAIN)
class MainView(context: Context) : PageView(context) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_page_main, this, true)
        bindContainerView(fl_container)
        setBackgroundColor(Color.WHITE)

        btn_main1.setOnClickListener { MultiViewNavigator.jump(Paths.PATH_MAIN01) }

        btn_main2.setOnClickListener { MultiViewNavigator.jump(Paths.PATH_MAIN02) }
    }

    override fun onOriginShow(isInit: Boolean, params: Any?) {
        super.onOriginShow(isInit, params)

        VLog.d("isInit: $isInit")

        if (params is DataParam) {
            VLog.d("isInit: $isInit params:${params.type}")
        }
    }

}