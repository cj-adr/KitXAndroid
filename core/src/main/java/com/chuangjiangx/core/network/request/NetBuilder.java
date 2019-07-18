package com.chuangjiangx.core.network.request;

import android.text.TextUtils;
import android.util.Log;

import com.chuangjiangx.core.network.callback.ErrorNetCallback;
import com.chuangjiangx.core.network.callback.NetCallback;
import com.chuangjiangx.core.network.error.HttpException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @author: yangshuiqiang
 * Time:2017/11/27 16:24
 */

public class NetBuilder {

    private CompositeDisposable mCompositeDisposable;
    private AuthExpiredListener mAuthExpiredListener;

    public NetBuilder(CompositeDisposable compositeDisposable, AuthExpiredListener authExpiredListener) {
        this.mCompositeDisposable = compositeDisposable;
        this.mAuthExpiredListener = authExpiredListener;
    }


    public <T> Disposable request(Flowable<T> flowable, final Consumer<T> consumer
            , final NetCallback... netCallbacks) {
        for (NetCallback netCallback : netCallbacks) {
            if (netCallback != null) {
                netCallback.onStart();
            }
        }
        Disposable disposable = flowable
                .doFinally(() -> {
                    for (NetCallback netCallback : netCallbacks) {
                        if (netCallback != null) {
                            netCallback.onComplete();
                        }
                    }
                })
                .subscribe(t -> {
                    consumer.accept(t);
                    for (NetCallback netCallback : netCallbacks) {
                        if (netCallback != null) {
                            netCallback.onRequestSuccess();
                        }
                    }
                }, throwable -> {
                    if (!TextUtils.isEmpty(throwable.getMessage()))
                        Log.e(NetBuilder.class.getSimpleName(), throwable.getMessage());
                    if (throwable instanceof UnknownHostException || throwable instanceof ConnectException
                            || throwable instanceof SocketTimeoutException) {
                        HttpException httpException = new HttpException(HttpException.NET_ERROR_CODE
                                , "网络请求失败，请检查网络后重试");
                        for (ErrorNetCallback errorNetCallback : netCallbacks) {
                            if (errorNetCallback != null) {
                                errorNetCallback.onRequestFail(httpException);
                            }
                        }
                    } else if (throwable instanceof HttpException) {
                        //000006错误，表示未登录，跳转至登录页
                        if ("000006".equals(((HttpException) throwable).getErrCode())) {
                            mAuthExpiredListener.onAuthExpired();
                        } else {
                            for (ErrorNetCallback errorNetCallback : netCallbacks) {
                                if (errorNetCallback != null) {
                                    errorNetCallback.onRequestFail((HttpException) throwable);
                                }
                            }
                        }
                    } else {
                        HttpException httpException = new HttpException("", throwable.getMessage());
                        for (ErrorNetCallback errorNetCallback : netCallbacks) {
                            errorNetCallback.onRequestFail(httpException);
                        }
                    }
                });

        if (mCompositeDisposable != null) {
            mCompositeDisposable.add(disposable);
        }
        return disposable;
    }
}