package com.chuangjiangx.print;

import android.content.Context;

import com.chuangjiangx.print.info.IPrintInfo;
import com.chuangjiangx.print.info.PrintPaper;
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
     *
     * @param context   上下文
     * @param printable 使用的打印方式
     * @param paperSize 打印机尺寸
     */
    public void init(Context context, Printable printable, IPaperSize paperSize) {
        this.mPrintable = printable;
        this.mPaperSize = paperSize;

        this.mPrintable.init(context);
    }

    /**
     * 获取尺寸规格
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
    public void print(PrintPaper paper) {
        this.print(paper.getPrintList());
    }

    /**
     * 打印
     */
    public void print(List<IPrintInfo> list) {
        if (null == list || list.isEmpty()) {
            PrintLogUtils.d("打印内容为空！");
            return;
        }

        if (null == mPrintable) {
            PrintLogUtils.d("mPrintable为空！");
            return;
        }

        if (!checkPrintable()) {
            PrintLogUtils.d("Printable不可用！");
            // 不可用
            if (!mPrintable.canReconnect()) {
                return;
            }

            // 重新连接
            mPrintable.reconnect();
        }

        // 清除缓存
        mPrintable.initPrinter();
        // 打印内容
        mPrintable.print(list);
        // 打印完成，切纸
        mPrintable.cutPaper();
    }

}
