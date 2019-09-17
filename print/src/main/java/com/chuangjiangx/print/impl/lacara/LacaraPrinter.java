package com.chuangjiangx.print.impl.lacara;

import android.content.Context;
import android.graphics.Bitmap;

import com.chuangjiangx.print.impl.DefaultPrintable;

/**
 * 拉卡拉打印
 */
public class LacaraPrinter extends DefaultPrintable {

    @Override
    public void init(Context context) {
        LacaraPrinterUtils.getInstance().init(context);
    }

    @Override
    public void close() {
        LacaraPrinterUtils.getInstance().close();
    }

    @Override
    public boolean isAvailable() {
        return LacaraPrinterUtils.getInstance().isAvailable();
    }

    @Override
    public void printText(String text, boolean isCenter, boolean isLarge, boolean isBold) {
        LacaraPrinterUtils.getInstance().printText(text, isCenter, isLarge, isBold);
    }

    @Override
    public void printBarCode(String barCode, int width, int height) {
        LacaraPrinterUtils.getInstance().printBarCode(barCode, width, height);
    }

    @Override
    public void printQrCode(String qrCode, int width, int height) {
        LacaraPrinterUtils.getInstance().printQrCode(qrCode, width, height);
    }

    @Override
    public void printBitmap(Bitmap bitmap) {
        LacaraPrinterUtils.getInstance().printBitmap(bitmap);
    }

    @Override
    public void feedPaper(int line) {
        LacaraPrinterUtils.getInstance().feedPaper(line);
    }

    @Override
    public void cutPaper() {
        LacaraPrinterUtils.getInstance().cutPaper();
    }

}
