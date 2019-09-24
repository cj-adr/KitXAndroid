package com.chuangjiangx.print.impl.usb;

import android.content.Context;
import android.graphics.Bitmap;

import com.chuangjiangx.print.impl.DefaultPrintable;

/**
 * 使用外置USB打印机
 */
public class UsbPrinter extends DefaultPrintable {

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
    public void printText(String text, boolean center, boolean largeSize, boolean bold) {
//        UsbPrinterUtils.getInstance().setAlign(center ? 1 : 0);
        UsbPrinterUtils.getInstance().bold(bold);
        UsbPrinterUtils.getInstance().setTextSize(largeSize ? 1 : 0);
        UsbPrinterUtils.getInstance().printText(text);
    }

    @Override
    public void printBarCode(String barCode, int width, int height) {
        UsbPrinterUtils.getInstance().setAlign(0);
        UsbPrinterUtils.getInstance().bold(false);
        UsbPrinterUtils.getInstance().setTextSize(0);

        UsbPrinterUtils.getInstance().printBarCode(barCode, width, height);
    }

    @Override
    public void printQrCode(String qrCode, int moduleSize, int errorLevel) {
        UsbPrinterUtils.getInstance().setAlign(0);
        UsbPrinterUtils.getInstance().bold(false);
        UsbPrinterUtils.getInstance().setTextSize(0);

        UsbPrinterUtils.getInstance().printQrCode(qrCode, moduleSize, errorLevel);
    }

    @Override
    public void printBitmap(Bitmap bitmap) {
        if (null == bitmap) {
            return;
        }

        UsbPrinterUtils.getInstance().setAlign(0);
        UsbPrinterUtils.getInstance().bold(false);
        UsbPrinterUtils.getInstance().setTextSize(0);

        UsbPrinterUtils.getInstance().printBitmap(bitmap);
    }

    @Override
    public void feedPaper(int line) {
        UsbPrinterUtils.getInstance().setAlign(0);
        UsbPrinterUtils.getInstance().bold(false);
        UsbPrinterUtils.getInstance().setTextSize(0);

        UsbPrinterUtils.getInstance().printLine(line);
    }

    @Override
    public void cutPaper() {
        UsbPrinterUtils.getInstance().setAlign(0);
        UsbPrinterUtils.getInstance().bold(false);
        UsbPrinterUtils.getInstance().setTextSize(0);

        UsbPrinterUtils.getInstance().cutPager();
    }

}
