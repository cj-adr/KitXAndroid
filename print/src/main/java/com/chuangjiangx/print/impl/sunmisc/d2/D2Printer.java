package com.chuangjiangx.print.impl.sunmisc.d2;

import android.content.Context;
import android.graphics.Bitmap;

import com.chuangjiangx.print.Printable;

/**
 * 商米D2系列，使用外置打印机
 */
public class D2Printer implements Printable {

    @Override
    public int getType() {
        return PrintType.SM_D2;
    }

    @Override
    public void init(Context context) {
        UsbPrinterUtils.getInstance().init(context);
    }

    @Override
    public void close() {
        UsbPrinterUtils.getInstance().close();
    }

    @Override
    public boolean isAvailable() {
        return UsbPrinterUtils.getInstance().isAvailable();
    }

    @Override
    public boolean hasPaper() {
        return UsbPrinterUtils.getInstance().isAvailable();
    }

    @Override
    public void printText(String text, boolean center, boolean largeSize, boolean bold) {
        UsbPrinterUtils.getInstance().setAlign(center ? 1 : 0);
        UsbPrinterUtils.getInstance().bold(bold);
        UsbPrinterUtils.getInstance().setTextSize(largeSize ? 3 : 0);
        UsbPrinterUtils.getInstance().printTextNewLine(text);
    }

    @Override
    public void printBitmap(Bitmap bitmap) {
        if (null == bitmap) {
            return;
        }

        UsbPrinterUtils.getInstance().setAlign(1);
        UsbPrinterUtils.getInstance().printBitmap(bitmap);
        UsbPrinterUtils.getInstance().setAlign(0);
        feedPaper(4);
    }

    @Override
    public void feedPaper(int line) {
        UsbPrinterUtils.getInstance().printLine(line);
    }

    @Override
    public void cutPaper() {
        UsbPrinterUtils.getInstance().cutPager();
    }

}
