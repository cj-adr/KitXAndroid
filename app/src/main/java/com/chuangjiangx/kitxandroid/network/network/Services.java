package com.chuangjiangx.kitxandroid.network.network;

import com.chuangjiangx.core.network.callback.NetCallback;
import com.chuangjiangx.core.network.handler.ModelHandler;
import com.chuangjiangx.core.network.request.NetBuilder;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class Services {

    public void getBase(NetBuilder netBuilder, Consumer<BaseBean> consumer, NetCallback... netCallback) {
        Flowable<BaseBean> flowable = RetrofitClient.getServer().getBase()
                .subscribeOn(Schedulers.io())
                .map(new ModelHandler<BaseBean>())
                .observeOn(AndroidSchedulers.mainThread());
        netBuilder.request(flowable, consumer, netCallback);
    }


    public Disposable getBaseList(int page, NetBuilder netBuilder, Consumer<BaseListBean> consumer, NetCallback... netCallback) {
        Flowable<BaseListBean> flowable = RetrofitClient.getServer().getBaseList(page)
                .subscribeOn(Schedulers.single())
                .map(new ModelHandler<BaseListBean>())
                .observeOn(AndroidSchedulers.mainThread());
        return netBuilder.request(flowable, consumer, netCallback);
    }
}
