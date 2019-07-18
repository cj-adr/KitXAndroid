package com.chuangjiangx.print.info;

import com.chuangjiangx.print.size.IPaperSize;

/**
 * 打印文本信息
 */
public class PrintTxtInfo {

    public String txt;
    public boolean isCenter;
    public boolean isBold; // 加粗
    public boolean isLargeSize;

    /**
     * 标题
     */
    public static PrintTxtInfo newTitle(IPaperSize paperSize, String txt, char c) {
        PrintTxtInfo info = new PrintTxtInfo();
        info.txt = paperSize.getTitleLine(txt, c);
        info.isCenter = true;
        info.isBold = true;
        info.isLargeSize = true;

        return info;
    }

    /**
     * 普通文本
     */
    public static PrintTxtInfo newNormalTxt(String txt) {
        PrintTxtInfo info = new PrintTxtInfo();
        info.txt = txt;

        return info;
    }

    /***
     * 三列文本
     */
    public static PrintTxtInfo new3ColTxt(IPaperSize paperSize, String start, String center, String end) {
        PrintTxtInfo info = new PrintTxtInfo();
        info.txt = paperSize.getPrintText(start, center, end);

        return info;
    }

    /**
     * 两列文本
     */
    public static PrintTxtInfo new2ColTxt(IPaperSize paperSize, String start, String end) {
        PrintTxtInfo info = new PrintTxtInfo();
        info.txt = paperSize.getPrintText(start, end);

        return info;
    }

}
