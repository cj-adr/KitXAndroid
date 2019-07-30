package com.chuangjiangx.core.network.request;

import android.text.TextUtils;
import android.util.Log;

import com.chuangjiangx.core.network.callback.ErrorNetCallback;
import com.chuangjiangx.core.network.callback.NetCallback;
import com.chuangjiangx.core.network.error.ErrorConsumer;
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
                }, new ErrorConsumer(mAuthExpiredListener, netCallbacks));

        if (mCompositeDisposable != null) {
            mCompositeDisposable.add(disposable);
        }
        return disposable;
    }
}