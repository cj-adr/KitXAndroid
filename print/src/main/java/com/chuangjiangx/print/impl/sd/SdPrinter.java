package com.chuangjiangx.print.impl.sd;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;

import com.chuangjiangx.print.PrintLogUtils;
import com.chuangjiangx.print.Printable;
import com.szsicod.print.escpos.PrinterAPI;
import com.szsicod.print.io.InterfaceAPI;
import com.szsicod.print.io.SerialAPI;

import java.io.File;

/**
 * 桑达打印
 */
public class SdPrinter implements Printable {

    @Override
    public int getType() {
        return PrintType.SD;
    }

    @Override
    public void init(Context context) {
        if (PrinterAPI.getInstance().isConnect()) {
            PrinterAPI.getInstance().disconnect();
        }

        InterfaceAPI io = new SerialAPI(new File("/dev/ttyS1"), 38400, 0);
        PrinterAPI.getInstance().connect(io);
    }

    @Override
    public void reconnect() {

    }

    @Override
    public void close() {
        PrinterAPI.getInstance().disconnect();
    }

    @Override
    public boolean isAvailable() {
        return PrinterAPI.getInstance().isConnect();
    }

    @Override
    public boolean canReconnect() {
        return false;
    }

    @Override
    public void printText(String text, boolean center, boolean largeSize, boolean bold) {
        try {
            if (center) {
                PrinterAPI.getInstance().setAlignMode(1);

            } else {
                PrinterAPI.getInstance().setAlignMode(0);
            }

            if (largeSize) {
                PrinterAPI.getInstance().setCharSize(1, 1);

            } else {
                PrinterAPI.getInstance().setCharSize(0, 0);
            }

            if (bold) {
                PrinterAPI.getInstance().setFontStyle(Typeface.BOLD);

            } else {
                PrinterAPI.getInstance().setFontStyle(Typeface.NORMAL);
            }

            PrinterAPI.getInstance().printString(text, "GBK", true);

        } catch (Exception e) {
            PrintLogUtils.e(e, "");
        }
    }

    @Override
    public void printBitmap(Bitmap bitmap) {
        try {
            PrinterAPI.getInstance().printRasterBitmap(bitmap);

        } catch (Exception e) {
            PrintLogUtils.e(e, "");
        }
    }

    @Override
    public void printBarCode(String barCode) {
        try {
            PrinterAPI.getInstance().printBarCode(0, 0, barCode);

        } catch (Exception e) {
            PrintLogUtils.e(e, "");
        }
    }

    @Override
    public void printQrCode(String qrCode) {
        try {
            PrinterAPI.getInstance().printQRCode2(qrCode);

        } catch (Exception e) {
            PrintLogUtils.e(e, "");
        }
    }

    @Override
    public void feedPaper(int line) {
        for (int i = 0; i < line; i++) {
            printText(" ", false, false, false);
        }
    }

    @Override
    public void cutPaper() {
        PrinterAPI.getInstance().halfCut();
    }

}
