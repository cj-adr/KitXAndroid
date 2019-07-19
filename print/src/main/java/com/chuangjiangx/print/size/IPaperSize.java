package com.chuangjiangx.print.size;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * 打印纸尺寸
 */
public abstract class IPaperSize {

    private int PADDING_SIZE = 2; // 间距
    int MAX_BYTE_SIZE = 0; // 一行允许的最大字节数
    int MAX_LEFT_SIZE = 0; // 左侧最大字节数
    int MAX_CENTER_SIZE = 0; // 中间最大字节数
    int MAX_RIGHT_SIZE = 0; // 右侧最大字节数
    int MAX_TITLE_BIG = 0;

    /**
     * 拼接标题（居中）
     */
    public String getTitleLine(String title, char c) {
        int len = title.getBytes(Charset.forName("GBK")).length;
        int sub = MAX_BYTE_SIZE - MAX_TITLE_BIG - len - PADDING_SIZE * 2;
        int left = sub / 2;
        int right = sub - left;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < left; i++) {
            sb.append(c);
        }
        sb.append(" ");
        sb.append(" ");
        sb.append(title);
        sb.append(" ");
        sb.append(" ");
        for (int i = 0; i < right; i++) {
            sb.append(c);
        }

        return sb.toString();
    }

    /**
     * 获取居中文本
     */
    public String getCenterTxt(String txt, char c) {
        int len = txt.getBytes(Charset.forName("GBK")).length;
        int sub = MAX_BYTE_SIZE - len - PADDING_SIZE * 2;
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
     * 获取签名线
     */
    public String getMarkLine(String name) {
        int len = name.getBytes(Charset.forName("GBK")).length;
        int sub = MAX_BYTE_SIZE - len;

        StringBuilder sb = new StringBuilder();
        sb.append(name);
        for (int i = 0; i < sub; i++) {
            sb.append('_');
        }

        return sb.toString();
    }

    /**
     * 获取分割线
     */
    public String getLine(char c) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < MAX_BYTE_SIZE; i++) {
            sb.append(c);
        }

        return sb.toString();
    }

    /**
     * 获取左中右样式文本
     */
    public String getPrintText(String start, String center, String end) {
        StringBuilder sb = new StringBuilder();

        List<String> startList = getTextList(start, MAX_LEFT_SIZE);
        List<String> centerList = getTextList(center, MAX_CENTER_SIZE);
        List<String> endList = getTextList(end, MAX_RIGHT_SIZE);

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

    private String getPrintLineText(String start, String center, String end) {
        StringBuilder sb = new StringBuilder();

        int startLen = start.getBytes(Charset.forName("GBK")).length;

        sb.append(start);
        int subLeft = MAX_LEFT_SIZE - startLen + PADDING_SIZE;
        for (int i = 0; i < subLeft; i++) {
            sb.append(" ");
        }

        int centerLen = center.getBytes(Charset.forName("GBK")).length;
        int subCenter = MAX_CENTER_SIZE - centerLen;
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
        int subRight = MAX_RIGHT_SIZE - endLen + PADDING_SIZE;

        for (int i = 0; i < subRight; i++) {
            sb.append(" ");
        }
        sb.append(end);

        return sb.toString();
    }

    private List<String> getTextList(String content, int limitSize) {
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

    private String substring(String content, int byteStart, int byteEnd) {
        char[] arrList = content.toCharArray();

        int realStart = fixIndex(byteStart, arrList);
        int realEnd = fixIndex(byteEnd, arrList);

        byte[] arr = content.getBytes(Charset.forName("GBK"));
        return new String(arr, realStart, realEnd - realStart, Charset.forName("GBK"));
    }

    private int fixIndex(int index, char[] arrList) {
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
    public String getPrintText(String start, String end) {
        StringBuilder sb = new StringBuilder();

        int startLen = start.getBytes(Charset.forName("GBK")).length;
        int endLen = end.getBytes(Charset.forName("GBK")).length;

        // 需要补的长度
        int sub = MAX_BYTE_SIZE - startLen - endLen;

        sb.append(start);

        for (int i = 0; i < sub; i++) {
            sb.append(" ");
        }
        sb.append(end);

        return sb.toString();
    }

}
