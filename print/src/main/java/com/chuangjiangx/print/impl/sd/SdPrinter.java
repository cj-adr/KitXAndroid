package com.chuangjiangx.print.impl.sd;

import android.content.Context;
import android.graphics.Typeface;

import com.chuangjiangx.print.PrintLogUtils;
import com.chuangjiangx.print.impl.AbstractPrintable;
import com.chuangjiangx.print.info.PrintBarCodeInfo;
import com.chuangjiangx.print.info.PrintImgInfo;
import com.chuangjiangx.print.info.PrintQrCodeInfo;
import com.chuangjiangx.print.info.PrintTxtInfo;
import com.chuangjiangx.print.info.PrintWrapInfo;
import com.szsicod.print.escpos.PrinterAPI;
import com.szsicod.print.io.InterfaceAPI;
import com.szsicod.print.io.SerialAPI;

import java.io.File;

/**
 * 桑达打印
 */
public class SdPrinter extends AbstractPrintable {

    @Override
    public void init(Context context) {
        if (PrinterAPI.getInstance().isConnect()) {
            PrinterAPI.getInstance().disconnect();
        }

        InterfaceAPI io = new SerialAPI(new File("/dev/ttyS1"), 38400, 0);
        PrinterAPI.getInstance().connect(io);
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
    public void initPrinter() {
    }

    @Override
    public void cutPaper() {
        PrinterAPI.getInstance().halfCut();
    }

    @Override
    protected void printTxt(PrintTxtInfo info) {
        try {
            PrinterAPI.getInstance().setAlignMode(info.isCenter ? 1 : 0);
            PrinterAPI.getInstance().setCharSize(info.isLargeSize ? 1 : 0, info.isLargeSize ? 1 : 0);
            PrinterAPI.getInstance().setFontStyle(info.isBold ? Typeface.BOLD : Typeface.NORMAL);
            PrinterAPI.getInstance().printString(info.txt, "GBK", true);

        } catch (Exception e) {
            PrintLogUtils.e(e, "");
        }
    }

    @Override
    protected void printImg(PrintImgInfo info) {
        try {
            PrinterAPI.getInstance().printRasterBitmap(info.img);

        } catch (Exception e) {
            PrintLogUtils.e(e, "");
        }
    }

    @Override
    protected void printWrap(PrintWrapInfo info) {
        PrinterAPI.getInstance().printAndFeedLine(info.count);
    }

    @Override
    protected void printQrCode(PrintQrCodeInfo info) {
        try {
            PrinterAPI.getInstance().printQRCode(info.code, info.width, false);

        } catch (Exception e) {
            PrintLogUtils.e(e, "");
        }
    }

    @Override
    protected void printBarCode(PrintBarCodeInfo info) {
        try {
            PrinterAPI.getInstance().printBarCode(0, 0, info.code);

        } catch (Exception e) {
            PrintLogUtils.e(e, "");
        }
    }

}
