package com.chuangjiangx.print.info;

/**
 * 打印BarCode
 */
public class PrintBarCodeInfo implements IPrintInfo {

    public String code; // 文本
    public int width = 2; //  条码宽度, 取值2至6, 默认2
    public int height = 162; // 条码高度, 取值1到255, 默认162

    public PrintBarCodeInfo(String code) {
        this.code = code;
    }

    public PrintBarCodeInfo(String code, int width, int height) {
        this.code = code;
        this.width = width;
        this.height = height;
    }

}
