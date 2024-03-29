package com.chuangjiangx.print.impl.usb;

import android.content.Context;

import com.chuangjiangx.print.impl.AbstractPrintable;
import com.chuangjiangx.print.info.PrintBarCodeInfo;
import com.chuangjiangx.print.info.PrintImgInfo;
import com.chuangjiangx.print.info.PrintQrCodeInfo;
import com.chuangjiangx.print.info.PrintTxtInfo;
import com.chuangjiangx.print.info.PrintWrapInfo;

/**
 * 使用外置USB打印机
 */
public class UsbPrinter extends AbstractPrintable {

    @Override
    public void init(Context context) {
        UsbPrinterUtils.getInstance().init(context);
    }

    @Override
    public void close() {
        UsbPrinterUtils.getInstance().close();
    }

    @Override
    public boolean isAvailable() {
        return UsbPrinterUtils.getInstance().isAvailable();
    }

    @Override
    public void initPrinter() {
        UsbPrinterUtils.getInstance().initPrinter();
    }

    @Override
    public void cutPaper() {
        UsbPrinterUtils.getInstance().cutPaper();
    }

    @Override
    protected void printTxt(PrintTxtInfo info) {
        UsbPrinterUtils.getInstance().printText(info.txt, info.isCenter, info.isBold, info.isLargeSize, info.hasUnderline);
    }

    @Override
    protected void printImg(PrintImgInfo info) {
        UsbPrinterUtils.getInstance().printBitmap(info.img, info.position);
    }

    @Override
    protected void printWrap(PrintWrapInfo info) {
        UsbPrinterUtils.getInstance().printWrapLine(info.count);
    }

    @Override
    protected void printQrCode(PrintQrCodeInfo info) {
        UsbPrinterUtils.getInstance().printQrCode(info.code, info.width);
    }

    @Override
    protected void printBarCode(PrintBarCodeInfo info) {
        UsbPrinterUtils.getInstance().printBarCode(info.code, info.width, info.height);
    }

}
