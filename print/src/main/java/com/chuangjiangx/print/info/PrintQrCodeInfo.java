package com.chuangjiangx.print.info;

/**
 * 打印QrCode
 */
public class PrintQrCodeInfo implements IPrintInfo {

    public String code;
    public int width;
    public int height;

    public PrintQrCodeInfo(String code) {
        this.code = code;
    }

    public PrintQrCodeInfo(String code, int width, int height) {
        this.code = code;
        this.width = width;
        this.height = height;
    }

}
