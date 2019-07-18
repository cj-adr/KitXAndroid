package com.chuangjiangx.print.impl.sunmisc.t2;

import android.content.Context;
import android.graphics.Bitmap;

import com.chuangjiangx.print.Printable;

/**
 * 商米T2系列，使用内置打印
 */
public class T2Printer implements Printable {

    @Override
    public int getType() {
        return PrintType.SM_T2;
    }

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
        return AidlUtil.getInstance().isConnect();
    }

    @Override
    public boolean hasPaper() {
        return AidlUtil.getInstance().hasPaper();
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
    public void printBitmap(Bitmap bitmap) {
        AidlUtil.getInstance().printBitmap(bitmap);
    }

    @Override
    public void feedPaper(int line) {
        AidlUtil.getInstance().feed(1);
    }

    @Override
    public void cutPaper() {
        AidlUtil.getInstance().cut();
    }

}
