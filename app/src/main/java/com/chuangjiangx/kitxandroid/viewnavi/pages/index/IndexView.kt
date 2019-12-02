package com.chuangjiangx.kitxandroid.viewnavi.pages.index

import android.content.Context
import android.view.LayoutInflater
import com.chuangjiangx.kitxandroid.R
import com.chuangjiangx.kitxandroid.viewnavi.DataParam
import com.chuangjiangx.kitxandroid.viewnavi.pages.Paths
import com.chuangjiangx.viewnavi.MultiViewNavigator
import com.chuangjiangx.viewnavi.compiler.annotation.Navigator
import com.chuangjiangx.viewnavi.impl.PageView
import com.chuangjiangx.viewnavi.info.ViewIntent
import kotlinx.android.synthetic.main.view_page_index.view.*

@Navigator(path = Paths.PATH_INDEX)
class IndexView(context: Context) : PageView(context) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_page_index, this, true)
        bindContainerView(fl_container)

        btn_main.setOnClickListener {
            MultiViewNavigator.jump(
                ViewIntent(
                    Paths.PATH_MAIN,
                    DataParam(1)
                )
            )
        }
        btn_about.setOnClickListener {
            MultiViewNavigator.jump(Paths.PATH_ABOUT)
            MultiViewNavigator.jump(Paths.PATH_DIALOG)
        }
        btn_close.setOnClickListener { MultiViewNavigator.finish() }
        btn_close_about.setOnClickListener { MultiViewNavigator.finishByKey(Paths.PATH_ABOUT) }
    }

}