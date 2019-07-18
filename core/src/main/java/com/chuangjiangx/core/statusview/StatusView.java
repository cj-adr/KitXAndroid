package com.chuangjiangx.core.statusview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.core.content.ContextCompat;

import com.chuangjiangx.core.R;
import com.chuangjiangx.core.network.callback.NetCallback;
import com.chuangjiangx.core.network.error.HttpException;
import com.chuangjiangx.core.network.itf.ListSourceInitialization;
import com.chuangjiangx.core.network.itf.SourceInitialization;
import com.chuangjiangx.core.utils.MeasureUtils;

/**
 * author : zhuna
 * time   : 2019/03/06
 * desc   : 不支持xml
 */
@SuppressLint("ViewConstructor")
public class StatusView extends FrameLayout implements NetCallback, View.OnClickListener {

    /**
     * 表示数据初始化是否完成
     */
    private boolean mIsInitialized;

    private ListSourceInitialization mListSourceInitialization;

    private SourceInitialization mSourceInitialization;

    private View mRootView;

    private View mItemError, mItemLoading;



    public StatusView(Context context, SourceInitialization sourceInitialization) {
        super(context);
        mSourceInitialization = sourceInitialization;
        initView();
    }

    public StatusView(Context context, ListSourceInitialization listSourceInitialization) {
        super(context);
        mListSourceInitialization = listSourceInitialization;
        initView();
    }


    public StatusView attach(Activity activity) {
        attach(activity.findViewById(android.R.id.content), MeasureUtils.dp2px(activity, 55));
        return this;
    }

    public StatusView attach(FrameLayout frameLayout) {
        attach(frameLayout, 0);
        return this;
    }

    public StatusView attach(Activity activity, int topMargin) {
        attach(activity.findViewById(android.R.id.content), 0);
        return this;
    }

    private void attach(FrameLayout container, int topMargin) {
        LayoutParams layoutParams = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        layoutParams.topMargin = topMargin;
        layoutParams.bottomMargin = 0;
        container.addView(this, layoutParams);
    }


    private void initView() {
        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.backgroundColor));
        mRootView = LayoutInflater.from(getContext()).inflate(
                R.layout.kitx_layout_status, this, false);
        mItemError = mRootView.findViewById(R.id.item_error);
        mItemLoading = mRootView.findViewById(R.id.item_loading);
        mRootView.setOnClickListener(this);
        addView(mRootView);
    }

    @Override
    public void onStart() {
        if (!mIsInitialized) {
            setVisibility(VISIBLE);
            mItemError.setVisibility(GONE);
            mItemLoading.setVisibility(VISIBLE);
            mRootView.setClickable(false);
        }
    }

    @Override
    public void onRequestSuccess() {
        if (!mIsInitialized) {
            mIsInitialized = true;
            setVisibility(GONE);
        }
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onRequestFail(HttpException e) {
        if (!mIsInitialized) {
            mRootView.setClickable(true);
            mItemError.setVisibility(VISIBLE);
            mItemLoading.setVisibility(GONE);
        }
    }

    @Override
    public void onClick(View view) {
        if (mListSourceInitialization != null && !mIsInitialized) {
            mListSourceInitialization.listSourceInit(1);
        }

        if (mSourceInitialization != null && !mIsInitialized) {
            mSourceInitialization.sourceInit();
        }
    }

    /**
     * 初始化，将重新显示StatusView
     */
    public void invalidateStatus() {
        mIsInitialized = false;
    }

    /**
     * 表示是否初始化完成
     */
    public boolean isInited() {
        return mIsInitialized;
    }
}
