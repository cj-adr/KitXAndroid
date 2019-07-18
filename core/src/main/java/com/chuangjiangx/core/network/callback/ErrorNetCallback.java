package com.chuangjiangx.core.network.callback;


import com.chuangjiangx.core.network.error.HttpException;

/**
 * @author yangshuiqiang
 * Time:2017/11/22 15:47
 */

public interface ErrorNetCallback {
    /**
     * 接口请求失败的处理
     */
    void onRequestFail(HttpException e);
}