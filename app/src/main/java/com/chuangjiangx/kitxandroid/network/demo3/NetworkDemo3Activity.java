package com.chuangjiangx.kitxandroid.network.demo3;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chuangjiangx.core.statusview.StatusView;
import com.chuangjiangx.core.network.itf.ListSourceInitialization;
import com.chuangjiangx.core.recylerview.loadmore.LoadCallback;
import com.chuangjiangx.core.recylerview.loadmore.LoadManage;
import com.chuangjiangx.core.refresh.RefreshCallback;
import com.chuangjiangx.core.toast.ToastCallback;
import com.chuangjiangx.core.utils.MeasureUtils;
import com.chuangjiangx.kitxandroid.R;
import com.chuangjiangx.kitxandroid.network.base.BaseActivity;
import com.chuangjiangx.kitxandroid.network.demo1.NetworkDemo1Activity;
import com.chuangjiangx.kitxandroid.network.network.Services;

import io.reactivex.disposables.Disposable;

public class NetworkDemo3Activity extends BaseActivity implements ListSourceInitialization {

    private StatusView mStatusView;

    private SwipeRefreshLayout mRefreshLayout;

    private RecyclerView mRecyclerView;

    private LoadManage<String> mLoadManage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_demo3);
        mRefreshLayout = findViewById(R.id.refresh_layout);
        mRecyclerView = findViewById(R.id.recycler_view);
        mStatusView = new StatusView(NetworkDemo3Activity.this, NetworkDemo3Activity.this)
                .attach(NetworkDemo3Activity.this, MeasureUtils.dp2px(NetworkDemo3Activity.this, 96));

        mLoadManage = LoadManage.newInstance(this, mRecyclerView, new Demo3Adapter());
        listSourceInit(1);
    }

    public void focusRefresh(View view) {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();//忽视之前的网络请求结果
        }
        mStatusView.invalidateStatus();
        listSourceInit(1);
    }

    private Disposable mDisposable;

    @Override
    public void listSourceInit(int page) {
        mDisposable = new Services().getBaseList(page, mNetBuilder, baseBean -> {
                    mLoadManage.refresh(page, baseBean.getNames());
                }, mStatusView, new RefreshCallback(mRefreshLayout, NetworkDemo3Activity.this)
                , new ToastCallback(mStatusView, false), new LoadCallback(mLoadManage, page));
    }


}
