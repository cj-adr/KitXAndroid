package com.chuangjiangx.viewnavi

import com.chuangjiangx.viewnavi.compiler.info.NavigatorInfo
import com.chuangjiangx.viewnavi.info.ViewIntent
import com.chuangjiangx.viewnavi.interfaces.IPageChange
import com.chuangjiangx.viewnavi.utils.PagePathUtil
import java.util.*
import kotlin.collections.LinkedHashMap
import kotlin.collections.set

/**
 * 多 ViewNavigator 管理，一个项目中可以有多个 ViewNavigator
 */
object MultiViewNavigator {

    private const val DEFAULT_TAG = "tag"
    private val mViewNavigator: LinkedHashMap<String, ViewNavigator> = LinkedHashMap()
    private val mPageChangeListener: ArrayList<IPageChange> = ArrayList()

    @JvmStatic
    fun initPaths(paths: ArrayList<NavigatorInfo>) {
        PagePathUtil.init(paths)
    }

    @JvmStatic
    @JvmOverloads
    fun add(navigator: ViewNavigator, tag: String = DEFAULT_TAG) {
        mViewNavigator[tag] = navigator
    }

    @JvmStatic
    @JvmOverloads
    fun jump(pathIndex: String, tag: String = DEFAULT_TAG) {
        mViewNavigator[tag]?.jump(pathIndex)

        notifyChange()
    }

    @JvmStatic
    @JvmOverloads
    fun jump(intent: ViewIntent, tag: String = DEFAULT_TAG) {
        mViewNavigator[tag]?.jump(intent)

        notifyChange()
    }

    @JvmStatic
    @JvmOverloads
    fun back(minDeep: Int = 2, tag: String = DEFAULT_TAG): Boolean {
        val result = mViewNavigator[tag]?.back(minDeep) ?: false

        notifyChange()

        return result
    }

    @JvmStatic
    @JvmOverloads
    fun onShow(tag: String = DEFAULT_TAG) {
        mViewNavigator[tag]?.onShow()
    }

    @JvmStatic
    @JvmOverloads
    fun onHide(tag: String = DEFAULT_TAG) {
        mViewNavigator[tag]?.onHide()
    }

    @JvmStatic
    @JvmOverloads
    fun finishByKey(vararg keys: String, tag: String = DEFAULT_TAG) {
        mViewNavigator[tag]?.finishByKey(*keys)

        notifyChange()
    }

    @JvmStatic
    @JvmOverloads
    fun finish(tag: String = DEFAULT_TAG) {
        mViewNavigator[tag]?.finish()

        notifyChange()
    }

    @JvmStatic
    fun clear() {
        mViewNavigator.entries.forEach { it.value.finish() }
        mViewNavigator.clear()

        mPageChangeListener.clear()
    }

    @JvmStatic
    fun addChangeListener(listener: IPageChange) {
        mPageChangeListener.add(listener)
    }

    private fun notifyChange() {
        mPageChangeListener.forEach { it.onPageChange() }
    }

}