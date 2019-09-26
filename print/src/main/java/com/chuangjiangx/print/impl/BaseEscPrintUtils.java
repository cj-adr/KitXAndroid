package com.chuangjiangx.print.impl;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.chuangjiangx.print.PrintLogUtils;
import com.chuangjiangx.print.escpos.BarUtils;
import com.chuangjiangx.print.escpos.ESCPOSUtil;
import com.google.zxing.BarcodeFormat;

/**
 * 支持ESC/POS指令打印的基类
 */
public abstract class BaseEscPrintUtils {

    protected abstract void write(byte[] bytes);

    /**
     * 恢复初始状态
     */
    public void initPrinter() {
        write(ESCPOSUtil.init());
    }

    /**
     * 切纸
     */
    public void cutPaper() {
        printWrapLine(5);
        write(ESCPOSUtil.cut());
        write(ESCPOSUtil.init());
    }

    /**
     * 打印文字
     */
    public void printText(String txt, boolean isCenter, boolean isBold, boolean isLargeSize, boolean hasUnderline) {
        if (TextUtils.isEmpty(txt)) {
            return;
        }

        setAlign(isCenter ? 1 : 0);
        bold(isBold);
        setTextSize(isLargeSize);
        setUnderline(hasUnderline);

        try {
            byte[] bytes = txt.getBytes("gb2312");
            write(bytes);

        } catch (Exception e) {
            PrintLogUtils.e(e, "");
        }

        printWrapLine(1);

        // 恢复默认
        setAlign(0);
        bold(false);
        setTextSize(false);
        setUnderline(false);
    }

    /**
     * 打印空行
     */
    public void printWrapLine(int size) {
        write(ESCPOSUtil.printWrapLine(size));
    }

    /**
     * 打印一维条形码
     */
    public void printBarCode(String data, int width, int height) {
        Bitmap bitmap = BarUtils.encodeAsBitmap(data, BarcodeFormat.CODE_128, 300, 80);
        printBitmap(bitmap, 1);

//        setAlign(1);
//
//        write(ESCPOSUtil.printBarCode(data, width, height));
//
//        printWrapLine(1);
//        setAlign(0);
    }

    /**
     * 打印二维码
     */
    public void printQrCode(String data, int moduleSize) {
        Bitmap bitmap = BarUtils.encodeAsBitmap(data, BarcodeFormat.QR_CODE, 300, 300);
        printBitmap(bitmap, 1);

//        setAlign(1);
//
//        write(ESCPOSUtil.printQrCode(qrCode, moduleSize, 3));
//
//        printWrapLine(1);
//        setAlign(0);
    }

    /**
     * 打印图片
     */
    public void printBitmap(Bitmap bitmap, int position) {
        if (null == bitmap) {
            return;
        }

        setAlign(position);

        write(ESCPOSUtil.printBitmap(bitmap, bitmap.getWidth()));

        printWrapLine(1);
        setAlign(0);
    }

    /**
     * 设置对齐方式
     */
    private void setAlign(int position) {
        write(ESCPOSUtil.setAlign(position));
    }

    /**
     * 字体加粗
     */
    private void bold(boolean isBold) {
        write(ESCPOSUtil.setFontBold(isBold));
    }

    /**
     * 设置字体大小
     */
    private void setTextSize(boolean isLarge) {
        write(ESCPOSUtil.setFontSize(1, isLarge ? 2 : 1));
    }

    /**
     * 设置下划线
     */
    private void setUnderline(boolean underline) {
        write(ESCPOSUtil.setUnderline(underline ? 1 : 0));
    }

}
