package com.chuangjiangx.core.recylerview.loadmore;

import com.chuangjiangx.core.network.callback.NetCallback;
import com.chuangjiangx.core.network.error.HttpException;

public class LoadCallback implements NetCallback {
    private boolean mLastQuestOk;
    private LoadManage mLoadManage;
    private int mPage;

    public LoadCallback(LoadManage loadManage, int page) {
        mLoadManage = loadManage;
        mPage = page;
    }

    @Override
    public void onStart() {
        mLoadManage.getLoadView().onStart();
    }

    @Override
    public void onRequestSuccess() {
        mLastQuestOk = true;
        mLoadManage.getLoadView().onRequestSuccess();
        mLoadManage.getLoadView().setIndex(mPage);
    }

    @Override
    public void onComplete() {
        if (mLastQuestOk) {
            mLoadManage.getLoadView().onComplete();
            mLoadManage.getLoadView().calculateStatus(mLoadManage.getAdapter().getDisplayCount());
            mLoadManage.getEmptyView().setVisible(mLoadManage.getAdapter().getDisplayCount()
                    , mLoadManage.getRecyclerView().getHeight());
        }
    }

    @Override
    public void onRequestFail(HttpException e) {
        mLastQuestOk = false;
        mLoadManage.getLoadView().onRequestFail(e);
    }
}
