package com.chuangjiangx.print.impl;

import android.content.Context;

import com.chuangjiangx.print.Printable;
import com.chuangjiangx.print.info.IPrintInfo;
import com.chuangjiangx.print.info.PrintBarCodeInfo;
import com.chuangjiangx.print.info.PrintImgInfo;
import com.chuangjiangx.print.info.PrintQrCodeInfo;
import com.chuangjiangx.print.info.PrintTxtInfo;
import com.chuangjiangx.print.info.PrintWrapInfo;

import java.util.List;

/**
 * 空实现
 */
public abstract class AbstractPrintable implements Printable {

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
        return true;
    }

    @Override
    public boolean canReconnect() {
        return false;
    }

    @Override
    public void initPrinter() {
    }

    @Override
    public void print(List<IPrintInfo> list) {
        for (IPrintInfo info : list) {
            dispatch(info);
        }
    }

    @Override
    public void cutPaper() {
    }

    private void dispatch(IPrintInfo info) {
        if (info instanceof PrintTxtInfo) {
            printTxt((PrintTxtInfo) info);
            return;
        }

        if (info instanceof PrintImgInfo) {
            printImg((PrintImgInfo) info);
            return;
        }

        if (info instanceof PrintWrapInfo) {
            printWrap((PrintWrapInfo) info);
            return;
        }

        if (info instanceof PrintQrCodeInfo) {
            printQrCode((PrintQrCodeInfo) info);
            return;
        }

        if (info instanceof PrintBarCodeInfo) {
            printBarCode((PrintBarCodeInfo) info);
        }
    }

    protected abstract void printTxt(PrintTxtInfo info);

    protected abstract void printImg(PrintImgInfo info);

    protected abstract void printWrap(PrintWrapInfo info);

    protected abstract void printQrCode(PrintQrCodeInfo info);

    protected abstract void printBarCode(PrintBarCodeInfo info);

}
