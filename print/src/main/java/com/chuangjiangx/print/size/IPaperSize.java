package com.chuangjiangx.print.size;

/**
 * 打印纸尺寸
 */
public abstract class IPaperSize {

    public int PADDING_SIZE = 2; // 间距
    public int MAX_BYTE_SIZE = 0; // 一行允许的最大字节数
    public int MAX_LEFT_SIZE = 0; // 左侧最大字节数
    public int MAX_CENTER_SIZE = 0; // 中间最大字节数
    public int MAX_RIGHT_SIZE = 0; // 右侧最大字节数
    public int MAX_TITLE_BIG = 0;

}
