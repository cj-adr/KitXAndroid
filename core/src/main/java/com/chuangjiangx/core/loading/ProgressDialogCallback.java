package com.chuangjiangx.core.loading;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.chuangjiangx.core.network.callback.NetCallback;
import com.chuangjiangx.core.network.error.HttpException;


public class ProgressDialogCallback implements NetCallback {

    private ProgressDialog mProgressDialog;

    public ProgressDialogCallback(Context context, View contentView) {
        mProgressDialog = new ProgressDialog(context, contentView);
    }

    public ProgressDialogCallback(Activity activity) {
        mProgressDialog = new ProgressDialog(activity, activity.findViewById(android.R.id.content));
    }

    @Override
    public void onStart() {
        mProgressDialog.show();
    }

    @Override
    public void onRequestSuccess() {

    }

    @Override
    public void onComplete() {
        mProgressDialog.hide();
    }

    @Override
    public void onRequestFail(HttpException e) {

    }
}
