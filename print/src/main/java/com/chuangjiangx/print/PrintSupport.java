package com.chuangjiangx.print;

import android.content.Context;

import androidx.annotation.NonNull;

import com.chuangjiangx.print.info.PrintImgInfo;
import com.chuangjiangx.print.info.PrintInfo;
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
    private boolean checkPrintable() {
        return null != mPrintable && mPrintable.isAvailable() && mPrintable.hasPaper();
    }

    /**
     * 打印
     */
    public void print(List<PrintInfo> list) {
        if (!checkPrintable()) {
            return;
        }

        if (null == list || list.isEmpty()) {
            return;
        }

        // 打印
        for (PrintInfo info : list) {
            print(info);
        }

        mPrintable.cutPaper();
    }

    private void print(PrintInfo info) {
        if (info.data instanceof PrintTxtInfo) {
            PrintTxtInfo txtInfo = (PrintTxtInfo) info.data;
            mPrintable.printText(txtInfo.txt, txtInfo.isCenter, txtInfo.isLargeSize, txtInfo.isBold);
            return;
        }

        if (info.data instanceof PrintImgInfo) {
            PrintImgInfo imgInfo = (PrintImgInfo) info.data;
            mPrintable.printBitmap(imgInfo.img);
            return;
        }

        if (info.data instanceof PrintWrapInfo) {
            PrintWrapInfo wrapInfo = (PrintWrapInfo) info.data;
            mPrintable.feedPaper(wrapInfo.count);
        }
    }

}
