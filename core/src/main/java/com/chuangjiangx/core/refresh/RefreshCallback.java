package com.chuangjiangx.core.refresh;


import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chuangjiangx.core.network.callback.NetCallback;
import com.chuangjiangx.core.network.error.HttpException;
import com.chuangjiangx.core.network.itf.ListSourceInitialization;
import com.chuangjiangx.core.network.itf.SourceInitialization;

/**
 * @author: yangshuiqiang
 * Time:2017/11/30 23:38
 */

public class RefreshCallback implements NetCallback {

    private SwipeRefreshLayout mSwipeRefreshLayout;

    public RefreshCallback(SwipeRefreshLayout swipeRefreshLayout
            , final SourceInitialization sourceInitialization) {
        this.mSwipeRefreshLayout = swipeRefreshLayout;
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sourceInitialization.sourceInit();
            }
        });
    }

    public RefreshCallback(SwipeRefreshLayout swipeRefreshLayout
            , final ListSourceInitialization listSourceInitialization) {
        this.mSwipeRefreshLayout = swipeRefreshLayout;
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listSourceInitialization.listSourceInit(1);
            }
        });
    }

    @Override
    public void onRequestFail(HttpException e) {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onRequestSuccess() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onComplete() {

    }
}
