package com.chuangjiangx.print.impl.bluetooth;

import android.content.Context;
import android.graphics.Bitmap;

import com.chuangjiangx.print.Printable;

/**
 * 蓝牙打印
 */
public class BluetoothPrinter implements Printable {

    public BluetoothPrinter() {
    }

    @Override
    public int getType() {
        return PrintType.BLUETOOTH;
    }

    @Override
    public void init(Context context) {
        BluetoothPrinterUtils.getInstance().init(context);
    }

    @Override
    public void close() {
        BluetoothPrinterUtils.getInstance().close();
    }

    @Override
    public boolean isAvailable() {
        return BluetoothPrinterUtils.getInstance().isAvailable();
    }

    @Override
    public boolean hasPaper() {
        return BluetoothPrinterUtils.getInstance().isAvailable();
    }

    @Override
    public void printText(String text, boolean isCenter, boolean isLarge, boolean isBold) {
        BluetoothPrinterUtils.getInstance().printText(text, isCenter, isLarge, isBold);
    }

    @Override
    public void printBitmap(Bitmap bitmap) {
        BluetoothPrinterUtils.getInstance().printBitmap(bitmap);
    }

    @Override
    public void feedPaper(int line) {
        BluetoothPrinterUtils.getInstance().feedPaper(line);
    }

    @Override
    public void cutPaper() {
        BluetoothPrinterUtils.getInstance().cutPaper();
    }

}
