package com.chuangjiangx.print.impl.bluetooth;

import android.content.Context;

import com.chuangjiangx.print.impl.AbstractPrintable;
import com.chuangjiangx.print.info.PrintBarCodeInfo;
import com.chuangjiangx.print.info.PrintImgInfo;
import com.chuangjiangx.print.info.PrintQrCodeInfo;
import com.chuangjiangx.print.info.PrintTxtInfo;
import com.chuangjiangx.print.info.PrintWrapInfo;

/**
 * 蓝牙打印
 */
public class BluetoothPrinter extends AbstractPrintable {

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
    public void initPrinter() {
        BluetoothPrinterUtils.getInstance().initPrinter();
    }

    @Override
    public void cutPaper() {
        BluetoothPrinterUtils.getInstance().cutPaper();
    }

    @Override
    protected void printTxt(PrintTxtInfo info) {
        BluetoothPrinterUtils.getInstance().printText(info.txt, info.isCenter, info.isBold, info.isLargeSize, info.hasUnderline);
    }

    @Override
    protected void printImg(PrintImgInfo info) {
        BluetoothPrinterUtils.getInstance().printBitmap(info.img, info.position);
    }

    @Override
    protected void printWrap(PrintWrapInfo info) {
        BluetoothPrinterUtils.getInstance().printWrapLine(info.count);
    }

    @Override
    protected void printQrCode(PrintQrCodeInfo info) {
        BluetoothPrinterUtils.getInstance().printQrCode(info.code, info.width);
    }

    @Override
    protected void printBarCode(PrintBarCodeInfo info) {
        BluetoothPrinterUtils.getInstance().printBarCode(info.code, info.width, info.height);
    }

}
