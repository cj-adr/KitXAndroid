package com.chuangjiangx.core.network.error;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.chuangjiangx.core.network.callback.ErrorNetCallback;
import com.chuangjiangx.core.network.error.HttpException;
import com.chuangjiangx.core.network.request.AuthExpiredListener;
import com.chuangjiangx.core.network.request.NetBuilder;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.functions.Consumer;

public class ErrorConsumer implements Consumer<Throwable> {

    private AuthExpiredListener mAuthExpiredListener;
    private ErrorNetCallback[] mErrorNetCallbacks;

    public ErrorConsumer(AuthExpiredListener authExpiredListener, ErrorNetCallback... errorNetCallbacks) {
        mAuthExpiredListener = authExpiredListener;
        mErrorNetCallbacks = errorNetCallbacks;
    }

    @Override
    public void accept(Throwable throwable) throws Exception {
        if (!TextUtils.isEmpty(throwable.getMessage()))
            Log.e(NetBuilder.class.getSimpleName(), throwable.getMessage());
        if (throwable instanceof UnknownHostException || throwable instanceof ConnectException
                || throwable instanceof SocketTimeoutException) {
            HttpException httpException = new HttpException(HttpException.NET_ERROR_CODE
                    , "网络异常 请检查你的网络");
            for (ErrorNetCallback errorNetCallback : mErrorNetCallbacks) {
                if (errorNetCallback != null) {
                    errorNetCallback.onRequestFail(httpException);
                }
            }
        } else if (throwable instanceof HttpException) {
            //000006错误，表示未登录，跳转至登录页
            if ("000006".equals(((HttpException) throwable).getErrCode())) {
                mAuthExpiredListener.onAuthExpired();
            } else {
                for (ErrorNetCallback errorNetCallback : mErrorNetCallbacks) {
                    if (errorNetCallback != null) {
                        errorNetCallback.onRequestFail((HttpException) throwable);
                    }
                }
            }
        } else {
            HttpException httpException = new HttpException("", throwable.getMessage());
            for (ErrorNetCallback errorNetCallback : mErrorNetCallbacks) {
                errorNetCallback.onRequestFail(httpException);
            }
        }
    }
}
