package com.chuangjiangx.print.impl.lacara;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import com.chuangjiangx.print.Printable;

/**
 * 拉卡拉打印
 */
public class LacaraPrinter implements Printable {

    private Context mContext;

    @Override
    public int getType() {
        return PrintType.LACARA;
    }

    @Override
    public void init(Context context) {
        this.mContext = context;
        Intent intent = new Intent(mContext, LacaraPrinterService.class);
        intent.putExtra(LacaraPrinterService.EXTRA_TYPE, 0);
        mContext.startService(intent);
    }

    @Override
    public void close() {
        Intent intent = new Intent(mContext, LacaraPrinterService.class);
        intent.putExtra(LacaraPrinterService.EXTRA_TYPE, 2);
        mContext.startService(intent);
    }

    @Override
    public boolean isAvailable() {
        return false;
    }

    @Override
    public boolean hasPaper() {
        return false;
    }

    @Override
    public void printText(String text, boolean isCenter, boolean isLarge, boolean isBold) {
        Intent intent = new Intent(mContext, LacaraPrinterService.class);
        intent.putExtra(LacaraPrinterService.EXTRA_TYPE, 3);
        intent.putExtra(LacaraPrinterService.EXTRA_TEXT, text);
        intent.putExtra(LacaraPrinterService.EXTRA_CENTER, isCenter);
        intent.putExtra(LacaraPrinterService.EXTRA_LARGE, isLarge);
        mContext.startService(intent);
    }

    @Override
    public void printBitmap(Bitmap bitmap) {

    }

    @Override
    public void printBarCode(String barCode) {
        Intent intent = new Intent(mContext, LacaraPrinterService.class);
        intent.putExtra(LacaraPrinterService.EXTRA_TYPE, 4);
        intent.putExtra(LacaraPrinterService.EXTRA_TEXT, barCode);
        mContext.startService(intent);
    }

    @Override
    public void printQrCode(String qrCode) {
        Intent intent = new Intent(mContext, LacaraPrinterService.class);
        intent.putExtra(LacaraPrinterService.EXTRA_TYPE, 5);
        intent.putExtra(LacaraPrinterService.EXTRA_TEXT, qrCode);
        mContext.startService(intent);
    }

    @Override
    public void feedPaper(int line) {
        Intent intent = new Intent(mContext, LacaraPrinterService.class);
        intent.putExtra(LacaraPrinterService.EXTRA_TYPE, 7);
        mContext.startService(intent);
    }

    @Override
    public void cutPaper() {

    }

    public void flushPrint() {
        Intent intent = new Intent(mContext, LacaraPrinterService.class);
        intent.putExtra(LacaraPrinterService.EXTRA_TYPE, 1);
        mContext.startService(intent);
    }

}
