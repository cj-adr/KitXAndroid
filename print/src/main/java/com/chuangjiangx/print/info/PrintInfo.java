package com.chuangjiangx.print.info;

/**
 * 打印信息
 */
public class PrintInfo<T> {

    public T data;

    private PrintInfo(T data) {
        this.data = data;
    }

    public static <T> PrintInfo newData(T info) {
        return new PrintInfo<>(info);
    }

}
