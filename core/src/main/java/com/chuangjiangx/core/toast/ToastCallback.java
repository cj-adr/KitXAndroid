package com.chuangjiangx.core.toast;


import com.chuangjiangx.core.statusview.StatusView;
import com.chuangjiangx.core.network.callback.NetCallback;
import com.chuangjiangx.core.network.error.HttpException;

/**
 * @author: yangshuiqiang
 * Time:2018/1/12
 */
public class ToastCallback implements NetCallback {


    private boolean mIsOnlyServer = false;

    private StatusView mStatusView;

    public ToastCallback() {
    }

    public ToastCallback(boolean isOnlyServer) {
        mIsOnlyServer = isOnlyServer;
    }

    public ToastCallback(StatusView statusView, boolean isOnlyServer) {
        mStatusView = statusView;
        mIsOnlyServer = isOnlyServer;
    }

    @Override
    public void onRequestFail(HttpException e) {
        if (mIsOnlyServer) {
            if (!e.isNetError()) {
                toast(e);
            }
        } else {
            toast(e);
        }
    }


    private void toast(HttpException e) {
        if (mStatusView != null && !mStatusView.isInited() && e.isNetError()) return;
        SingleToast.get().shortShow(e.getErrMsg());
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onRequestSuccess() {

    }

    @Override
    public void onComplete() {
    }
}
