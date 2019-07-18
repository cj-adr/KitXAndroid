package com.chuangjiangx.core.network.bean;

/**
 * <pre>
 *     author : 杨水强
 *     time   : 2018/7/9
 *     desc   : 网络请求基础数据类型，包含数据
 *     version: 1.0
 * </pre>
 */

public class CommonBean<T> extends ResponseBean {

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
