package com.chuangjiangx.print.info;

/**
 * 打印QrCode
 */
public class PrintQrCodeInfo implements IPrintInfo {

    public String code; // 文本
    public int width = 10; // 宽度 或者是 二维码块大小(单位:点, 取值 1 至 16 )
    public int height = 3; // 高度 或者是 二维码纠错等级(0 至 3)

    public PrintQrCodeInfo(String code) {
        this.code = code;
    }

    public PrintQrCodeInfo(String code, int width, int height) {
        this.code = code;
        this.width = width;
        this.height = height;
    }

}
