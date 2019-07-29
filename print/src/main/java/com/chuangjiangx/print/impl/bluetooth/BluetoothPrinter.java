package com.chuangjiangx.print.impl.bluetooth;

import android.content.Context;
import android.graphics.Bitmap;

import com.chuangjiangx.print.Printable;

/**
 * 蓝牙打印
 */
public class BluetoothPrinter implements Printable {

    private String address;
    private BluetoothConnectListener mListener;

    public interface BluetoothConnectListener {
        void onConnectSuccess(String address);

        void onConnectFail(String address, Throwable e);
    }

    public BluetoothPrinter(String address, BluetoothConnectListener listener) {
        this.address = address;
        this.mListener = listener;
    }

    @Override
    public int getType() {
        return PrintType.BLUETOOTH;
    }

    @Override
    public void init(Context context) {
        BluetoothPrinterUtils.getInstance().init(context, address, mListener);
    }

    @Override
    public void reconnect() {
        BluetoothPrinterUtils.getInstance().connectDevice(address);
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
    public boolean canReconnect() {
        return true;
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
    public void printBarCode(String barCode) {
        BluetoothPrinterUtils.getInstance().printBarCode(barCode);
    }

    @Override
    public void printQrCode(String qrCode) {
        BluetoothPrinterUtils.getInstance().printQrCode(qrCode);
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
