package com.chuangjiangx.print.info;

/**
 * 打印QrCode
 */
public class PrintQrCodeInfo implements IPrintInfo {

    public String code; // 文本
    public int width; // 二维码块大小(单位:点, 取值 1 至 16 )

    public PrintQrCodeInfo(String code) {
        this(code, 10);
    }

    public PrintQrCodeInfo(String code, int width) {
        this.code = code;

        if (width < 1 || width > 16) {
            this.width = 10;

        } else {
            this.width = width;
        }
    }

}
