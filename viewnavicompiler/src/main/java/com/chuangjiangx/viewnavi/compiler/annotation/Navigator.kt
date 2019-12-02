package com.chuangjiangx.viewnavi.compiler.annotation

/**
 * 导航注解
 *
 * @param path 导航路径 "init/index/test" 。默认以最后的'/'之前的为group
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Navigator(
    val path: String,
    val startMode: Int = 0
)