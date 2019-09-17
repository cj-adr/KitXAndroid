package com.chuangjiangx.print.info;

/**
 * 打印BarCode
 */
public class PrintBarCodeInfo implements IPrintInfo {

    public String code;
    public int width;
    public int height;

    public PrintBarCodeInfo(String code) {
        this.code = code;
    }

    public PrintBarCodeInfo(String code, int width, int height) {
        this.code = code;
        this.width = width;
        this.height = height;
    }

}
