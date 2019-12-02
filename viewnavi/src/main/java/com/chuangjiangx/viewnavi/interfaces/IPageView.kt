package com.chuangjiangx.viewnavi.interfaces

import android.view.ViewGroup
import com.chuangjiangx.viewnavi.info.ViewIntent

/**
 * 界面组件
 */
interface IPageView : ILife {

    /**
     * 绑定子容器视图
     */
    fun bindContainerView(containerView: ViewGroup)

    /**
     * 获取所属分组
     */
    fun getGroup(): String?

    /**
     * 获取绑定的子容器视图
     */
    fun getContainerView(): ViewGroup?

    /**
     * 获取视图层级
     */
    fun getDeep(): Int

    /**
     * 返回
     */
    fun back(): Boolean

    /**
     * 关闭指定界面
     */
    fun finishByKey(key: String): Boolean

    /**
     * 跳转到新界面
     */
    fun jump(intent: ViewIntent)

}