package com.chuangjiangx.print.info;

/**
 * 打印空行信息
 */
public class PrintWrapInfo implements IPrintInfo {

    /**
     * 空几行
     */
    public int count;

    public PrintWrapInfo(int count) {
        this.count = count;
    }

}
