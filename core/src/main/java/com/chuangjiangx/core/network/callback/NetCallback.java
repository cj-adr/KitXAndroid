package com.chuangjiangx.core.network.callback;

/**
 * @author: 杨水强
 * Time:2017/11/27 15:59
 */

public interface NetCallback extends ErrorNetCallback {

    void onStart();

    void onRequestSuccess();

    void onComplete();
}
