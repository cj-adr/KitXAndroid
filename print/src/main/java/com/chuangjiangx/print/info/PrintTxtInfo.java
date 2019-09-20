package com.chuangjiangx.print.info;

import com.chuangjiangx.print.PrintSupport;
import com.chuangjiangx.print.size.IPaperSize;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * 打印文本信息
 */
public class PrintTxtInfo implements IPrintInfo {

    public String txt;
    public boolean isCenter;
    public boolean isBold; // 加粗
    public boolean isLargeSize;

    public PrintTxtInfo(String txt) {
        this.txt = txt;
    }

    public PrintTxtInfo(String txt, boolean isCenter) {
        this.txt = txt;
        this.isCenter = isCenter;
    }

    public PrintTxtInfo(String txt, boolean isCenter, boolean isBold, boolean isLargeSize) {
        this.txt = txt;
        this.isCenter = isCenter;
        this.isBold = isBold;
        this.isLargeSize = isLargeSize;
    }

    /**
     * 构建列表对象
     */
    public static List<PrintTxtInfo> newList(List<String> list) {
        List<PrintTxtInfo> printList = new ArrayList<>();

        for (String print : list) {
            printList.add(new PrintTxtInfo(print));
        }

        return printList;
    }

    /**
     * 构建列表对象
     */
    public static List<PrintTxtInfo> newList(String... params) {
        List<PrintTxtInfo> printList = new ArrayList<>();

        if (null == params || params.length <= 0) {
            return printList;
        }

        for (String print : params) {
            printList.add(new PrintTxtInfo(print));
        }

        return printList;
    }

    /**
     * 标题
     */
    public static PrintTxtInfo newTitle(String txt, char c) {
        return new PrintTxtInfo(getCenterTxt(txt, c, true), true, true, true);
    }

    /***
     * 三列文本
     */
    public static PrintTxtInfo new3ColTxt(String start, String center, String end) {
        return new PrintTxtInfo(getPrintText(start, center, end));
    }

    /**
     * 两列文本
     */
    public static PrintTxtInfo new2ColTxt(String start, String end) {
        return new PrintTxtInfo(getPrintText(start, end));
    }

    /**
     * 居中文本
     */
    public static PrintTxtInfo newCenterTxt(String txt) {
        return newCenterTxt(txt, ' ');
    }

    /**
     * 居中文本
     */
    public static PrintTxtInfo newCenterTxt(String txt, char c) {
        return new PrintTxtInfo(getCenterTxt(txt, c), true);
    }

    /**
     * 分割线
     */
    public static PrintTxtInfo newSplitLine(char c) {
        return new PrintTxtInfo(getLine(c));
    }

    /**
     * 签名线
     */
    public static PrintTxtInfo newMarkLine(String name) {
        return new PrintTxtInfo(getMarkLine(name));
    }

    /**
     * 获取居中文本
     */
    private static String getCenterTxt(String txt, char c) {
        return getCenterTxt(txt, c, false);
    }

    /**
     * 获取居中文本（文字放大）
     */
    private static String getCenterTxt(String txt, char c, boolean isLargeSize) {
        IPaperSize paperSize = PrintSupport.getInstance().getPrintSize();

        int len = txt.getBytes(Charset.forName("GBK")).length;
        int sub = paperSize.MAX_BYTE_SIZE - (isLargeSize ? paperSize.MAX_TITLE_BIG : 0) - len - paperSize.PADDING_SIZE * 2;
        int left = sub / 2;
        int right = sub - left;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < left; i++) {
            sb.append(c);
        }
        sb.append(" ");
        sb.append(" ");
        sb.append(txt);
        sb.append(" ");
        sb.append(" ");
        for (int i = 0; i < right; i++) {
            sb.append(c);
        }

        return sb.toString();
    }

    /**
     * 获取分割线
     */
    private static String getLine(char c) {
        IPaperSize paperSize = PrintSupport.getInstance().getPrintSize();

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < paperSize.MAX_BYTE_SIZE; i++) {
            sb.append(c);
        }

        return sb.toString();
    }

    /**
     * 获取签名线
     */
    private static String getMarkLine(String name) {
        IPaperSize paperSize = PrintSupport.getInstance().getPrintSize();

        int len = name.getBytes(Charset.forName("GBK")).length;
        int sub = paperSize.MAX_BYTE_SIZE - len;

        StringBuilder sb = new StringBuilder();
        sb.append(name);
        for (int i = 0; i < sub; i++) {
            sb.append('_');
        }

        return sb.toString();
    }

    /**
     * 获取左中右样式文本
     */
    private static String getPrintText(String start, String center, String end) {
        IPaperSize paperSize = PrintSupport.getInstance().getPrintSize();

        StringBuilder sb = new StringBuilder();

        List<String> startList = getTextList(start, paperSize.MAX_LEFT_SIZE);
        List<String> centerList = getTextList(center, paperSize.MAX_CENTER_SIZE);
        List<String> endList = getTextList(end, paperSize.MAX_RIGHT_SIZE);

        int startSize = startList.size();
        int centerSize = centerList.size();
        int endSize = endList.size();
        int maxSize = Math.max(endSize, Math.max(startSize, centerSize));

        for (int i = 0; i < maxSize; i++) {
            sb.append(getPrintLineText(
                    i < startSize ? startList.get(i) : "",
                    i < centerSize ? centerList.get(i) : "",
                    i < endSize ? endList.get(i) : "")
            );
        }

        return sb.toString();
    }

    private static String getPrintLineText(String start, String center, String end) {
        IPaperSize paperSize = PrintSupport.getInstance().getPrintSize();

        StringBuilder sb = new StringBuilder();

        int startLen = start.getBytes(Charset.forName("GBK")).length;

        sb.append(start);
        int subLeft = paperSize.MAX_LEFT_SIZE - startLen + paperSize.PADDING_SIZE;
        for (int i = 0; i < subLeft; i++) {
            sb.append(" ");
        }

        int centerLen = center.getBytes(Charset.forName("GBK")).length;
        int subCenter = paperSize.MAX_CENTER_SIZE - centerLen;
        int subCenterLeft = subCenter / 2;
        int subCenterRight = subCenter - subCenterLeft;
        for (int i = 0; i < subCenterLeft; i++) {
            sb.append(" ");
        }
        sb.append(center);
        for (int i = 0; i < subCenterRight; i++) {
            sb.append(" ");
        }

        int endLen = end.getBytes(Charset.forName("GBK")).length;
        int subRight = paperSize.MAX_RIGHT_SIZE - endLen + paperSize.PADDING_SIZE;

        for (int i = 0; i < subRight; i++) {
            sb.append(" ");
        }
        sb.append(end);

        return sb.toString();
    }

    private static List<String> getTextList(String content, int limitSize) {
        List<String> list = new ArrayList<>();

        byte[] arr = content.getBytes(Charset.forName("GBK"));
        int len = arr.length;
        int count = len / limitSize;
        int less = len % limitSize;

        for (int i = 0; i < count; i++) {
            list.add(substring(content, i * limitSize, (i + 1) * limitSize));
        }

        if (less > 0) {
            list.add(substring(content, count * limitSize, len));
        }

        return list;
    }

    private static String substring(String content, int byteStart, int byteEnd) {
        char[] arrList = content.toCharArray();

        int realStart = fixIndex(byteStart, arrList);
        int realEnd = fixIndex(byteEnd, arrList);

        byte[] arr = content.getBytes(Charset.forName("GBK"));
        return new String(arr, realStart, realEnd - realStart, Charset.forName("GBK"));
    }

    private static int fixIndex(int index, char[] arrList) {
        if (index == 0) {
            return 0;
        }

        int realIndex = 0;
        int i = 0;
        for (char c : arrList) {
            int cLen = String.valueOf(c).getBytes(Charset.forName("GBK")).length;

            if (cLen == 1) {
                i += 1;
            }

            if (cLen == 2) {
                // 汉字
                i += 2;
            }

            if (i == index) {
                // 无需修复
                realIndex = index;
                break;
            }

            if (i > index) {
                // 回退一个
                realIndex = i - 2;
                break;
            }
        }

        return realIndex;
    }

    /**
     * 获取左右样式文本
     */
    private static String getPrintText(String start, String end) {
        IPaperSize paperSize = PrintSupport.getInstance().getPrintSize();

        StringBuilder sb = new StringBuilder();

        int startLen = start.getBytes(Charset.forName("GBK")).length;
        int endLen = end.getBytes(Charset.forName("GBK")).length;

        // 需要补的长度
        int sub = paperSize.MAX_BYTE_SIZE - startLen - endLen;

        sb.append(start);

        for (int i = 0; i < sub; i++) {
            sb.append(" ");
        }
        sb.append(end);

        return sb.toString();
    }

}
