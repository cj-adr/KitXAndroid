package com.chuangjiangx.print.impl.sunmisc;

import android.content.Context;

import com.chuangjiangx.print.impl.AbstractPrintable;
import com.chuangjiangx.print.info.PrintBarCodeInfo;
import com.chuangjiangx.print.info.PrintImgInfo;
import com.chuangjiangx.print.info.PrintQrCodeInfo;
import com.chuangjiangx.print.info.PrintTxtInfo;
import com.chuangjiangx.print.info.PrintWrapInfo;

/**
 * 商米内置打印(例如：T2系列)
 */
public class SmPrinter extends AbstractPrintable {

    @Override
    public void init(Context context) {
        SmPrinterUtils.getInstance().init(context);
    }

    @Override
    public void close() {
        SmPrinterUtils.getInstance().close();
    }

    @Override
    public boolean isAvailable() {
        return SmPrinterUtils.getInstance().isAvailable();
    }

    @Override
    public void initPrinter() {
        SmPrinterUtils.getInstance().initPrinter();
    }

    @Override
    public void cutPaper() {
        SmPrinterUtils.getInstance().cutPaper();
    }

    @Override
    protected void printTxt(PrintTxtInfo info) {
        SmPrinterUtils.getInstance().printText(info.txt, info.isCenter, info.isLargeSize, info.isBold, info.hasUnderline);
    }

    @Override
    protected void printImg(PrintImgInfo info) {
        SmPrinterUtils.getInstance().printBitmap(info.img, info.position);
    }

    @Override
    protected void printWrap(PrintWrapInfo info) {
        SmPrinterUtils.getInstance().printWrapLine(info.count);
    }

    @Override
    protected void printQrCode(PrintQrCodeInfo info) {
        SmPrinterUtils.getInstance().printQrCode(info.code, info.width);
    }

    @Override
    protected void printBarCode(PrintBarCodeInfo info) {
        SmPrinterUtils.getInstance().printBarCode(info.code, info.width, info.height);
    }

}
