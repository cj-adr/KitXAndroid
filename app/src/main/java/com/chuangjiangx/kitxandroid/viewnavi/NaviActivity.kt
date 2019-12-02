package com.chuangjiangx.kitxandroid.viewnavi

import android.os.Bundle
import com.chuangjiangx.kitxandroid.network.base.BaseActivity
import com.chuangjiangx.viewnavi.MultiViewNavigator
import com.chuangjiangx.viewnavi.ViewNavigator
import com.chuangjiangx.kitxandroid.viewnavi.pages.InitView
import com.chuangjiangx.kitxandroid.viewnavi.pages.Paths
import com.chuangjiangx.viewnavi.ViewPaths

/**
 * view导航demo
 */
class NaviActivity :BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val initView = InitView(this)
        setContentView(initView)

        // 初始化路径
        MultiViewNavigator.initPaths(ViewPaths.getPaths())

        // 添加导航器
        MultiViewNavigator.add(ViewNavigator(this, initView))

        // 跳转界面
        MultiViewNavigator.jump(Paths.PATH_INDEX)
    }

    override fun onBackPressed() {
        val result = MultiViewNavigator.back(2)
        if (!result) {
            super.onBackPressed()
        }
    }

    override fun onResume() {
        MultiViewNavigator.onShow()
        super.onResume()
    }

    override fun onPause() {
        MultiViewNavigator.onHide()
        super.onPause()
    }

    override fun onDestroy() {
        MultiViewNavigator.finish()
        super.onDestroy()
    }

}