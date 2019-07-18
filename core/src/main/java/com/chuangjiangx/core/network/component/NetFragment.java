package com.chuangjiangx.core.network.component;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.chuangjiangx.core.network.request.AuthExpiredListener;
import com.chuangjiangx.core.network.request.NetBuilder;

import io.reactivex.disposables.CompositeDisposable;

/**
 * @author: yangshuiqiang
 * Time:2017/12/1 0:37
 */

public abstract class NetFragment extends Fragment implements AuthExpiredListener {
    protected CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    protected NetBuilder mNetBuilder = new NetBuilder(mCompositeDisposable, NetFragment.this);


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        mCompositeDisposable.dispose();
        super.onDestroy();
    }
}
