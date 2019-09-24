package com.chuangjiangx.print.impl.sunmisc.t2;

import android.content.Context;
import android.graphics.Bitmap;

import com.chuangjiangx.print.impl.DefaultPrintable;

/**
 * 商米T2系列，使用内置打印
 */
public class T2Printer extends DefaultPrintable {

    @Override
    public void init(Context context) {
        AidlUtil.getInstance().init(context);
    }


    @Override
    public void close() {
        AidlUtil.getInstance().close();
    }

    @Override
    public boolean isAvailable() {
        return AidlUtil.getInstance().isConnect() && AidlUtil.getInstance().hasPaper();
    }

    @Override
    public void printText(String text, boolean center, boolean largeSize, boolean bold) {
        if (center) {
            AidlUtil.getInstance().printTextCenter(text, largeSize ? 40 : 28, bold, false);

        } else {
            AidlUtil.getInstance().printText(text, largeSize ? 40 : 28, bold, false);
        }
    }

    @Override
    public void printBarCode(String barCode, int width, int height) {
        AidlUtil.getInstance().printBarCode(barCode, width, height);
    }

    @Override
    public void printQrCode(String qrCode, int moduleSize, int errorLevel) {
        AidlUtil.getInstance().printQrCode(qrCode, moduleSize, errorLevel);
    }

    @Override
    public void printBitmap(Bitmap bitmap) {
        AidlUtil.getInstance().printBitmap(bitmap);
    }

    @Override
    public void feedPaper(int line) {
        AidlUtil.getInstance().feed(line);
    }

    @Override
    public void cutPaper() {
        feedPaper(2);
        AidlUtil.getInstance().cut();
    }

}
