package com.chuangjiangx.viewnavi

import android.content.Context
import com.chuangjiangx.viewnavi.info.ViewIntent
import com.chuangjiangx.viewnavi.interfaces.IPageView
import java.lang.ref.WeakReference

/**
 * 视图导航器
 *
 * @param context Context
 * @param pageView 入口界面
 */
class ViewNavigator(context: Context, private var pageView: IPageView?) {

    private var mContext: WeakReference<Context>? = null

    init {
        mContext = WeakReference(context)

        pageView?.onShow(true)
    }

    /**
     * 跳转界面
     */
    fun jump(path: String) {
        this.jump(ViewIntent(path))
    }

    /**
     * 跳转界面
     */
    fun jump(intent: ViewIntent) {
        pageView?.jump(intent)
    }

    /**
     * 返回
     */
    fun back(minDeep: Int = 2): Boolean {
        val deep = (pageView?.getDeep() ?: 0) + 1
        if (deep <= minDeep) {
            return false
        }

        val ret = pageView?.back() ?: false
        if (ret) {
            pageView?.onShow()
        }

        return ret
    }

    /**
     * 根据key关闭指定界面
     */
    fun finishByKey(vararg keys: String) {
        keys.forEach { pageView?.finishByKey(it) }

        pageView?.onShow()
    }

    /**
     * 关闭所有界面
     */
    fun finish() {
        pageView?.onRemove()
        mContext?.clear()
        pageView = null
        mContext = null
    }

    /**
     * 显示
     */
    fun onShow() {
        pageView?.onShow()
    }

    /**
     * 隐藏
     */
    fun onHide() {
        pageView?.onHide()
    }

}