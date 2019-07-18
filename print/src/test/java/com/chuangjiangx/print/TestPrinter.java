package com.chuangjiangx.print;

import android.content.Context;
import android.graphics.Bitmap;

public class TestPrinter implements Printable {

    @Override
    public int getType() {
        return PrintType.TEST;
    }

    @Override
    public void init(Context context) {

    }

    @Override
    public void close() {

    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public boolean hasPaper() {
        return true;
    }

    @Override
    public void printText(String text, boolean center, boolean largeSize, boolean bold) {
        System.out.println(text);
    }

    @Override
    public void printBitmap(Bitmap bitmap) {
        System.out.println("***********");
        System.out.println("**打印图片**");
        System.out.println("***********");
    }

    @Override
    public void feedPaper(int line) {
        for (int i = 0; i < line; i++) {
            System.out.println(" ");
        }
    }

    @Override
    public void cutPaper() {
        System.out.println(" ");
    }

}
