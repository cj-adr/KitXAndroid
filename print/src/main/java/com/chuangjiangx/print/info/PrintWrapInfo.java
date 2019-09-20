package com.chuangjiangx.print.info;

/**
 * 打印空行信息
 */
public class PrintWrapInfo implements IPrintInfo {

    // 空几行，默认空一行
    public int count = 1;

    public PrintWrapInfo() {
    }

    public PrintWrapInfo(int count) {
        if (count <= 0) {
            return;
        }

        this.count = count;
    }

}
