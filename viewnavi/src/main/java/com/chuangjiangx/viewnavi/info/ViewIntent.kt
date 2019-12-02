package com.chuangjiangx.viewnavi.info

import com.chuangjiangx.viewnavi.enums.Flags

/**
 * 界面跳转意图
 */
class ViewIntent @JvmOverloads constructor(
    val path: String, // 路径，与所需渲染的组件一一对应
    var params: Any? = null, // 携带的参数
    val flag: Flags = Flags.FLAG_NEW_TASK
)