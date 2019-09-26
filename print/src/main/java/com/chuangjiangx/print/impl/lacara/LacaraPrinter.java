package com.chuangjiangx.print.impl.lacara;

import android.content.Context;

import com.chuangjiangx.print.impl.AbstractPrintable;
import com.chuangjiangx.print.info.PrintBarCodeInfo;
import com.chuangjiangx.print.info.PrintImgInfo;
import com.chuangjiangx.print.info.PrintQrCodeInfo;
import com.chuangjiangx.print.info.PrintTxtInfo;
import com.chuangjiangx.print.info.PrintWrapInfo;

/**
 * 拉卡拉打印
 */
public class LacaraPrinter extends AbstractPrintable {

    @Override
    public void init(Context context) {
        LacaraPrinterUtils.getInstance().init(context);
    }

    @Override
    public void close() {
        LacaraPrinterUtils.getInstance().close();
    }

    @Override
    public boolean isAvailable() {
        return LacaraPrinterUtils.getInstance().isAvailable();
    }

    @Override
    public void initPrinter() {
        LacaraPrinterUtils.getInstance().initPrinter();
    }

    @Override
    public void cutPaper() {
        LacaraPrinterUtils.getInstance().cutPaper();
    }

    @Override
    protected void printTxt(PrintTxtInfo info) {
        LacaraPrinterUtils.getInstance().printText(info.txt, info.isCenter, info.isLargeSize, info.isBold, info.hasUnderline);
    }

    @Override
    protected void printImg(PrintImgInfo info) {
        LacaraPrinterUtils.getInstance().printBitmap(info.img, info.position);
    }

    @Override
    protected void printWrap(PrintWrapInfo info) {
        LacaraPrinterUtils.getInstance().printWrapLine(info.count);
    }

    @Override
    protected void printQrCode(PrintQrCodeInfo info) {
        LacaraPrinterUtils.getInstance().printQrCode(info.code, info.width);
    }

    @Override
    protected void printBarCode(PrintBarCodeInfo info) {
        LacaraPrinterUtils.getInstance().printBarCode(info.code, info.width, info.height);
    }

}
