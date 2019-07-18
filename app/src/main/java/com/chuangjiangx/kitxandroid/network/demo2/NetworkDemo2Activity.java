package com.chuangjiangx.kitxandroid.network.demo2;

import android.os.Bundle;
import android.view.View;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chuangjiangx.core.network.itf.SourceInitialization;
import com.chuangjiangx.core.refresh.RefreshCallback;
import com.chuangjiangx.core.statusview.StatusView;
import com.chuangjiangx.core.toast.ToastCallback;
import com.chuangjiangx.core.utils.MeasureUtils;
import com.chuangjiangx.kitxandroid.R;
import com.chuangjiangx.kitxandroid.network.base.BaseActivity;
import com.chuangjiangx.kitxandroid.network.network.Services;

public class NetworkDemo2Activity extends BaseActivity implements SourceInitialization {

    private StatusView mStatusView;

    private SwipeRefreshLayout mRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_demo2);
        mRefreshLayout = findViewById(R.id.refresh_layout);
        mStatusView = new StatusView(NetworkDemo2Activity.this, NetworkDemo2Activity.this)
                .attach(NetworkDemo2Activity.this, MeasureUtils.dp2px(NetworkDemo2Activity.this, 48));
        sourceInit();
    }

    @Override
    public void sourceInit() {
        //这里包括了三个netcallback，处理了初始化错误，初始化下拉刷新，初始化吐司
        new Services().getBase(mNetBuilder, baseBean -> {

                }, mStatusView, new RefreshCallback(mRefreshLayout, NetworkDemo2Activity.this)
                , new ToastCallback(mStatusView, false));
    }

    /**
     * 按钮点击事件
     */
    public void focusRefresh(View view) {
        mStatusView.invalidateStatus();
        sourceInit();
    }
}
