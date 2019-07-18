package com.chuangjiangx.core.network.component;


import androidx.appcompat.app.AppCompatActivity;

import com.chuangjiangx.core.network.request.AuthExpiredListener;
import com.chuangjiangx.core.network.request.NetBuilder;

import io.reactivex.disposables.CompositeDisposable;

public abstract class NetActivity extends AppCompatActivity implements AuthExpiredListener {


    protected CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    protected NetBuilder mNetBuilder = new NetBuilder(mCompositeDisposable, NetActivity.this);

    @Override
    protected void onDestroy() {
        mCompositeDisposable.dispose();
        super.onDestroy();
    }
}
