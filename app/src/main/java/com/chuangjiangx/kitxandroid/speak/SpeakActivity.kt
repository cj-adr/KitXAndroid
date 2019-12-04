package com.chuangjiangx.kitxandroid.speak

import android.os.Bundle
import com.chuangjiangx.core.speak.SpeakManager
import com.chuangjiangx.kitxandroid.R
import com.chuangjiangx.kitxandroid.network.base.BaseActivity
import kotlinx.android.synthetic.main.activity_speak.*

/**
 * view导航demo
 */
class SpeakActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speak)
        text.text="1.Application中初始化KitX.init()\n" +
                "2.Application中初始化SpeakManager.init()\n" +
                "3.Manifest中添加添加权限ACCESS_NETWORK_STATE 和NETWORK\n" +
                "4.配置kotlin环境\n" +
                "5.使用SpeakManager.speak()播报"
        SpeakManager.speak("123")
    }
}