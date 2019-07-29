package com.chuangjiangx.print;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 打印机接口，各类打印机需要适配该接口
 */
public interface Printable {

    @IntDef({PrintType.TEST, PrintType.SM_T2, PrintType.TZH, PrintType.SD, PrintType.LACARA, PrintType.BLUETOOTH, PrintType.USB})
    @Retention(RetentionPolicy.SOURCE)
    @interface PrintType {
        int TEST = -1;
        int SM_T2 = 1;
        int TZH = 2;
        int SD = 3;
        int LACARA = 4;
        int BLUETOOTH = 5;
        int USB = 6;
    }

    /**
     * 返回打印机类型
     */
    @PrintType
    int getType();

    /**
     * 初始化打印机，在Application中使用
     */
    void init(Context context);

    /**
     * 重连
     */
    void reconnect();

    /**
     * 关闭打印机，在application中使用
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
     * 打印图片
     */
    void printBitmap(Bitmap bitmap);

    /**
     * 打印条形码
     */
    void printBarCode(String barCode);

    /**
     * 打印二维码
     */
    void printQrCode(String qrCode);

    /**
     * 打印机走纸
     */
    void feedPaper(int line);

    /**
     * 切纸
     */
    void cutPaper();

}
