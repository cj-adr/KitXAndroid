package com.chuangjiangx.print.impl;

import android.content.Context;
import android.graphics.Bitmap;

import com.chuangjiangx.print.Printable;

public abstract class DefaultPrintable implements Printable {

    @Override
    public void init(Context context) {

    }

    @Override
    public void reconnect() {

    }

    @Override
    public void close() {

    }

    @Override
    public boolean isAvailable() {
        return false;
    }

    @Override
    public boolean canReconnect() {
        return false;
    }

    @Override
    public void printText(String text, boolean center, boolean largeSize, boolean bold) {

    }

    @Override
    public void printBarCode(String barCode, int width, int height) {

    }

    @Override
    public void printQrCode(String qrCode, int width, int height) {

    }

    @Override
    public void printBitmap(Bitmap bitmap) {

    }

    @Override
    public void feedPaper(int line) {

    }

    @Override
    public void cutPaper() {

    }

}
