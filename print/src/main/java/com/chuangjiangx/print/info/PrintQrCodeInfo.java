package com.chuangjiangx.print.info;

/**
 * 打印QrCode
 */
public class PrintQrCodeInfo implements IPrintInfo {

    public String code; // 文本
    public int width; // 大小

    public PrintQrCodeInfo(String code) {
        this(code, 0);
    }

    public PrintQrCodeInfo(String code, int width) {
        this.code = code;
        this.width = width;
    }

}
