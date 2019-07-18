package com.chuangjiangx.kitxandroid.network.demo1;

import android.os.Bundle;

import com.chuangjiangx.core.statusview.StatusView;
import com.chuangjiangx.core.network.itf.SourceInitialization;
import com.chuangjiangx.core.toast.ToastCallback;
import com.chuangjiangx.core.utils.MeasureUtils;
import com.chuangjiangx.kitxandroid.R;
import com.chuangjiangx.kitxandroid.network.base.BaseActivity;
import com.chuangjiangx.kitxandroid.network.network.Services;

public class NetworkDemo1Activity extends BaseActivity implements SourceInitialization {

    private StatusView mStatusView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_demo1);
        mStatusView = new StatusView(NetworkDemo1Activity.this, NetworkDemo1Activity.this)
                .attach(NetworkDemo1Activity.this, MeasureUtils.dp2px(NetworkDemo1Activity.this, 48));
        sourceInit();
    }

    @Override
    public void sourceInit() {
        new Services().getBase(mNetBuilder, baseBean -> {

        }, mStatusView, new ToastCallback(mStatusView, false));
    }

}