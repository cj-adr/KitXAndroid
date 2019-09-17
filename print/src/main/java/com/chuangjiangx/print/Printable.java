package com.chuangjiangx.print;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * 打印机接口，各类打印机需要适配该接口
 */
public interface Printable {

    /**
     * 初始化打印机，在Application中使用
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
     * 打印文本
     */
    void printText(String text, boolean center, boolean largeSize, boolean bold);

    /**
     * 打印条形码
     */
    void printBarCode(String barCode, int width, int height);

    /**
     * 打印二维码
     */
    void printQrCode(String qrCode, int width, int height);

    /**
     * 打印图片
     */
    void printBitmap(Bitmap bitmap);

    /**
     * 打印机走纸
     */
    void feedPaper(int line);

    /**
     * 切纸
     */
    void cutPaper();

}
