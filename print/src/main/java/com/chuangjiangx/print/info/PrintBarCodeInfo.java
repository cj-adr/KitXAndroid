package com.chuangjiangx.print.info;

/**
 * 条形码信息(Code128编码)
 */
public class PrintBarCodeInfo implements IPrintInfo {

    public String code; // 文本
    public int width; // 条码宽度
    public int height; // 条码高度

    public PrintBarCodeInfo(String code) {
        this(code, 0, 0);
    }

    public PrintBarCodeInfo(String code, int width, int height) {
        this.code = code;
        this.width = width;
        this.height = height;
    }

}
