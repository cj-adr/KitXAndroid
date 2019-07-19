package com.chuangjiangx.print.info;

/**
 * 打印QrCode
 */
public class PrintQrCodeInfo implements IPrintInfo {

    public String code;

    public PrintQrCodeInfo(String code) {
        this.code = code;
    }
}
