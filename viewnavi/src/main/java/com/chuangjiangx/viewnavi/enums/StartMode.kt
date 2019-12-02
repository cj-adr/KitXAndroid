package com.chuangjiangx.viewnavi.enums

/**
 * 启动标识
 */
enum class StartMode(private val type: Int) {

    Standard(0),
    SingleTask(1),
    SingleTop(2);

    companion object {

        fun findType(type: Int?): StartMode {
            return when (type) {
                Standard.type -> Standard
                SingleTask.type -> SingleTask
                SingleTop.type -> SingleTop
                else -> Standard
            }
        }
    }

}