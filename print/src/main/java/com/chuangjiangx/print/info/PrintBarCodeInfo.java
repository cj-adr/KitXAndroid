package com.chuangjiangx.print.info;

/**
 * 条形码信息(Code128编码)
 */
public class PrintBarCodeInfo implements IPrintInfo {

    public String code; // 文本
    public int width; //  条码宽度(像素), 取值2至6, 默认2
    public int height; // 条码高度(像素), 取值1到255, 默认162

    public PrintBarCodeInfo(String code) {
        this(code, 2, 162);
    }

    public PrintBarCodeInfo(String code, int height) {
        this(code, 2, height);
    }

    public PrintBarCodeInfo(String code, int width, int height) {
        this.code = code;

        if (width < 2 || width > 6) {
            this.width = 2;

        } else {
            this.width = width;
        }

        if (height < 1 || height > 255) {
            this.height = 162;

        } else {
            this.height = height;
        }
    }

}
