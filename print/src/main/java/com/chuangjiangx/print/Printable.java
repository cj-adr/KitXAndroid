package com.chuangjiangx.print;

import android.content.Context;

import com.chuangjiangx.print.info.IPrintInfo;

import java.util.List;

/**
 * 打印机接口，各类打印机需要适配该接口
 */
public interface Printable {

    /**
     * 初始化打印机，在Application中使用
     *
     * @param context 上下文
     */
    void init(Context context);

    /**
     * 重连
     */
    void reconnect();

    /**
     * 关闭打印机
     */
    void close();

    /**
     * 是否是可用的
     */
    boolean isAvailable();

    /**
     * 是否可以重连
     */
    boolean canReconnect();

    /**
     * 初始化打印机，清除缓存
     */
    void initPrinter();

    /**
     * 打印
     *
     * @param list 内容
     */
    void print(List<IPrintInfo> list);

    /**
     * 走到切纸位置，有切刀就切纸，没有就提示声音
     */
    void cutPaper();

}
