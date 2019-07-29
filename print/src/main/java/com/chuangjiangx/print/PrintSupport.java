package com.chuangjiangx.print;

import android.content.Context;

import androidx.annotation.NonNull;

import com.chuangjiangx.print.info.IPrintInfo;
import com.chuangjiangx.print.info.PrintBarCodeInfo;
import com.chuangjiangx.print.info.PrintImgInfo;
import com.chuangjiangx.print.info.PrintQrCodeInfo;
import com.chuangjiangx.print.info.PrintTxtInfo;
import com.chuangjiangx.print.info.PrintWrapInfo;
import com.chuangjiangx.print.size.IPaperSize;

import java.util.List;

/**
 * 打印支持类
 */
public final class PrintSupport {

    private Printable mPrintable;
    private IPaperSize mPaperSize;

    private PrintSupport() {
    }

    private static class PrintSupportInner {
        private static PrintSupport instance = new PrintSupport();
    }

    public static PrintSupport getInstance() {
        return PrintSupportInner.instance;
    }

    /**
     * 初始化打印机
     */
    public void init(@NonNull Context context, @NonNull Printable printable, IPaperSize paperSize) {
        this.mPrintable = printable;
        this.mPrintable.init(context);
        this.mPaperSize = paperSize;
    }

    /**
     * 获取小票规格
     */
    public IPaperSize getPrintSize() {
        return mPaperSize;
    }

    /**
     * 关闭打印机
     */
    public void close() {
        if (null == mPrintable) {
            return;
        }

        mPrintable.close();
    }

    /**
     * 检测打印机是否可用
     */
    public boolean checkPrintable() {
        return null != mPrintable && mPrintable.isAvailable();
    }

    /**
     * 打印
     */
    public void print(List<IPrintInfo> list) {
        if (null == list || list.isEmpty()) {
            return;
        }

        if (!checkPrintable()) {
            if (!mPrintable.canReconnect()) {
                return;
            }

            // 重新连接
            mPrintable.reconnect();
        }

        // 打印
        for (IPrintInfo info : list) {
            print(info);
        }

        mPrintable.cutPaper();
    }

    private void print(IPrintInfo info) {
        if (info instanceof PrintTxtInfo) {
            PrintTxtInfo txtInfo = (PrintTxtInfo) info;
            mPrintable.printText(txtInfo.txt, txtInfo.isCenter, txtInfo.isLargeSize, txtInfo.isBold);
            return;
        }

        if (info instanceof PrintImgInfo) {
            PrintImgInfo imgInfo = (PrintImgInfo) info;
            mPrintable.printBitmap(imgInfo.img);
            return;
        }

        if (info instanceof PrintWrapInfo) {
            PrintWrapInfo wrapInfo = (PrintWrapInfo) info;
            mPrintable.feedPaper(wrapInfo.count);
            return;
        }

        if (info instanceof PrintQrCodeInfo) {
            PrintQrCodeInfo qrCodeInfo = (PrintQrCodeInfo) info;
            mPrintable.printQrCode(qrCodeInfo.code);
            return;
        }

        if (info instanceof PrintBarCodeInfo) {
            PrintBarCodeInfo barCodeInfo = (PrintBarCodeInfo) info;
            mPrintable.printBarCode(barCodeInfo.code);
        }
    }

}
