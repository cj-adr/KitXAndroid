package com.chuangjiangx.kitxandroid.viewnavi.pages.dialog

import android.content.Context
import android.view.LayoutInflater
import com.chuangjiangx.kitxandroid.R
import com.chuangjiangx.kitxandroid.viewnavi.pages.Paths
import com.chuangjiangx.viewnavi.MultiViewNavigator
import com.chuangjiangx.viewnavi.compiler.annotation.Navigator
import com.chuangjiangx.viewnavi.impl.PageView
import kotlinx.android.synthetic.main.view_page_dialog.view.*

@Navigator(path = Paths.PATH_DIALOG)
class DialogView(context: Context) : PageView(context) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_page_dialog, this, true)

        btn_close.setOnClickListener { MultiViewNavigator.finishByKey(Paths.PATH_DIALOG) }
    }

}