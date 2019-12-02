package com.chuangjiangx.viewnavi.impl

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import com.chuangjiangx.viewnavi.enums.Flags
import com.chuangjiangx.viewnavi.enums.PageStatus
import com.chuangjiangx.viewnavi.enums.StartMode
import com.chuangjiangx.viewnavi.info.ViewIntent
import com.chuangjiangx.viewnavi.interfaces.IPageView
import com.chuangjiangx.viewnavi.utils.PagePathUtil
import com.chuangjiangx.viewnavi.utils.VLog

/**
 * 页面组件
 */
open class PageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), IPageView {

    private var status = PageStatus.UNKNOWN

    init {
        isClickable = true
        isFocusable = true
    }

    // 存放子界面
    private var mContainerView: ViewGroup? = null
    // 存放 mContainerView 里显示的界面
    private var mPages: ArrayList<Pair<String, IPageView>> = ArrayList()

    override fun bindContainerView(containerView: ViewGroup) {
        this.mContainerView = containerView
    }

    override fun getGroup(): String? {
        return PagePathUtil.findGroup(javaClass.name, null != mContainerView)
    }

    override fun getContainerView(): ViewGroup? {
        return mContainerView
    }

    override fun getDeep(): Int {
        var deep = mPages.size

        mPages.forEach {
            deep += it.second.getDeep()
        }

        return deep
    }

    override fun onShow(isInit: Boolean, params: Any?) {
        if (status != PageStatus.SHOW) {
            onOriginShow(isInit, params)
        }
        status = PageStatus.SHOW

        if (!isInit) {
            getTopView()?.second?.onShow(false, params)
        }
    }

    override fun onHide() {
        status = PageStatus.HIDE
        VLog.d("onHide: ${javaClass.simpleName}")
        getTopView()?.second?.onHide()
    }

    override fun onRemove() {
        status = PageStatus.REMOVE
        // 清空子界面
        VLog.d("onRemove: ${javaClass.simpleName}")
        clear()
    }

    /**
     * 处理返回事件，返回 true 表示事件已消费
     */
    override fun back(): Boolean {
        if (mPages.isNullOrEmpty()) {
            return false
        }

        handleBack()
        return true
    }

    /**
     * 关闭指定界面
     */
    override fun finishByKey(key: String): Boolean {
        if (mPages.isNullOrEmpty()) {
            return false
        }

        mPages.reversed().forEach {
            if (key == it.first) {
                closeItem(it)
                return true

            } else {
                if (it.second.finishByKey(key)) {
                    return true
                }
            }
        }

        return false
    }

    /**
     * 跳转到新界面
     */
    override fun jump(intent: ViewIntent) {
        val gotoGroup = PagePathUtil.getGroup(intent.path)
        val curGroup = getGroup()
        // 是同一组的
        if (gotoGroup == curGroup) {
            handleIntent(intent)
            return
        }

        // 非同一组
        jump(gotoGroup, intent)
    }

    //********************************************************//

    /**
     * 获取当前状态
     */
    open fun getCurState(): PageStatus {
        return status
    }

    open fun onOriginShow(isInit: Boolean, params: Any?) {
        VLog.d("onShow: $isInit ${javaClass.simpleName}")
    }

    //********************************************************//

    private fun handleBack() {
        val last = mPages.last()
        if (last.second.back()) {
            return
        }

        closeItem(last)
    }

    private fun closeItem(item: Pair<String, IPageView>) {
        try {
            val pageView = item.second

            if (pageView is PageView) {
                VLog.d("closeItem: ${javaClass.simpleName} -> ${pageView::class.java.simpleName}")

                pageView.onRemove()
                mPages.remove(item)
                getContainerView()?.removeView(pageView)
            }

        } catch (e: Exception) {
            VLog.e(e)
        }

    }

    private fun getTopView(): Pair<String, IPageView?>? {
        if (mPages.isNullOrEmpty()) {
            return null
        }

        return mPages.last()
    }

    private fun contain(intent: ViewIntent): Boolean {
        return null != findPageByPath(intent.path)
    }

    private fun isTop(intent: ViewIntent): Boolean {
        return contain(intent) && mPages.last().first == intent.path
    }

    private fun moveTop(intent: ViewIntent) {
        if (intent.flag == Flags.FLAG_CLEAR) {
            // 清除当前操作界面的以外界面
            clearByPath(intent.path)
        }

        val topView = getTopView()
        if (topView?.first == intent.path) {
            // 当前界面已在栈顶
            topView.second?.onShow(false, intent.params)
            return
        }

        // 移到栈顶
        val item = findPageByPath(intent.path) ?: return
        mPages.remove(item)
        mPages.add(item)
        val pageView = item.second

        if (pageView is PageView) {
            pageView.bringToFront()
        }

        topView?.second?.onHide()
        pageView.onShow(false, intent.params)
    }

    private fun addPage(intent: ViewIntent) {
        if (intent.flag == Flags.FLAG_CLEAR) {
            clear()
        }

        val pageView = PagePathUtil.getPageView(context, intent.path) ?: return
        val topView = getTopView()
        mPages.add(Pair(intent.path, pageView))

        try {
            // TODO 动画处理
            topView?.second?.onHide()
            getContainerView()?.addView(pageView)
            VLog.d("addPage: ${javaClass.simpleName} -> ${pageView::class.java.simpleName}")

            pageView.onShow(true, intent.params)

        } catch (e: Exception) {
            VLog.e(e)
        }
    }

    /**
     * 跳转子界面
     */
    private fun jump(gotoGroup: String, intent: ViewIntent) {
        mPages.reversed().forEach {
            if (gotoGroup.contains(it.first)) {
                it.second.jump(intent)
            }
        }
    }

    private fun handleIntent(intent: ViewIntent) {
        // 判断如何跳转
        val navigatorInfo = PagePathUtil.getNavigatorInfo(intent.path)
        when (StartMode.findType(navigatorInfo?.startMode)) {
            StartMode.Standard -> {
                // 每次都跳转到新界面
                addPage(intent)
            }

            StartMode.SingleTask -> {
                // 如果栈中有此界面，则移到栈顶，否则，新建界面
                if (contain(intent)) {
                    moveTop(intent)
                    return
                }

                addPage(intent)
            }

            StartMode.SingleTop -> {
                // 如果有栈中有此界面，且在栈顶，不再显示，否则，新建界面
                if (isTop(intent)) {
                    moveTop(intent)
                    return
                }

                addPage(intent)
            }
        }
    }

    /**
     * 清除所有子界面
     */
    private fun clear() {
        try {
            mPages.forEach { it.second.onRemove() }
            mPages.clear()
            getContainerView()?.removeAllViews()

        } catch (e: Exception) {
            VLog.e(e)
        }
    }

    private fun clearByPath(path: String) {
        var findIndex = -1
        val delList = ArrayList<Pair<String, IPageView>>()
        mPages.reversed().forEachIndexed { index, item ->
            if (findIndex == -1 && item.first == path) {
                findIndex = index
            }

            if (findIndex != -1 && findIndex != index) {
                delList.add(item)
            }
        }

        delList.forEach {
            val pageView = it.second
            if (pageView is PageView) {
                pageView.onRemove()
                mPages.remove(it)
                getContainerView()?.removeView(pageView)
            }
        }
    }

    private fun findPageByPath(path: String): Pair<String, IPageView>? {
        mPages.reversed().forEach {
            if (it.first == path) {
                return it
            }
        }

        return null
    }

}