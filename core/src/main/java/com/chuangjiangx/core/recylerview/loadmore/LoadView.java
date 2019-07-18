package com.chuangjiangx.core.recylerview.loadmore;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.chuangjiangx.core.R;
import com.chuangjiangx.core.network.callback.NetCallback;
import com.chuangjiangx.core.network.error.HttpException;
import com.chuangjiangx.core.network.itf.ListSourceInitialization;
import com.chuangjiangx.core.utils.MeasureUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @author: yangshuiqiang
 * Time:2017/11/26 20:46
 */

@SuppressLint("ViewConstructor")
public class LoadView extends FrameLayout implements NetCallback, View.OnClickListener {

    private List<String> mStatusText = Arrays.asList("正在努力加载中", "正在努力加载中"
            , "加载失败，请点击重试", "没有更多了", "正在努力加载中");

    private TextView mTextView;
    private int mStatus;
    private int mIndex = -1;

    private int mCount;

    private ProgressBar mProgressBar;

    private boolean hideAfterOver;
    private ListSourceInitialization mListSourceInitialization;

    private int mHeight = -1;

    /**
     * @param hideAfterOver 加载完成是否隐藏autoloadview
     */
    public LoadView(@NonNull Context context
            , ListSourceInitialization listSourceInitialization, boolean hideAfterOver) {
        super(context);
        this.mListSourceInitialization = listSourceInitialization;
        this.hideAfterOver = hideAfterOver;
        initView(context);
    }

    private void initView(Context context) {
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, MeasureUtils.dp2px(getContext(), 40)));
        View inflate = LayoutInflater.from(context).inflate(R.layout.kitx_layout_loadview, this, false);
        mTextView = (TextView) inflate.findViewById(R.id.tv_status);
        mProgressBar = (ProgressBar) inflate.findViewById(R.id.progress);
        LayoutParams layoutParams =
                new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        addView(inflate, layoutParams);
        setOnClickListener(this);
        setStatus(Status.STATUS_NORMAL);
    }

    public void setStatus(int status) {
        if (status < 0 || status > 4)
            throw new RuntimeException("setStatus for error code");
        this.mStatus = status;
        updateUI();
    }

    public void setIndex(int index) {
        mIndex = index;
    }

    public void calculateStatus(int count) {
        mCount = count;
        if (mIndex == 1) {
            if (count < mIndex * Page.EVERY_PAGE_COUNT) {
                setStatus(Status.STATUS_OVER);
            } else {
                setStatus(Status.STATUS_NORMAL);
            }
        } else {
            if (count < mIndex * Page.EVERY_PAGE_COUNT) {
                setStatus(Status.STATUS_OVER);
            } else {
                setStatus(Status.STATUS_NORMAL);
            }
        }
    }


    public void load() {
        mListSourceInitialization.listSourceInit(mIndex + 1);
    }

    public LoadAdapter.OnAutoLoadListener getOnAutoLoadListener() {
        return () -> {
            if (mStatus == Status.STATUS_NORMAL) {
                setStatus(Status.STATUS_LOAD);
                load();
            }
        };
    }


    @Override
    public void onClick(View view) {
        if (mStatus == Status.STATUS_ERROR) {
            setStatus(Status.STATUS_LOAD);
            load();
        }
    }


    private void updateUI() {
        mTextView.setText(mStatusText.get(mStatus));
        if (mCount == 0 || mStatus == Status.STATUS_OVER && hideAfterOver) {
            if (mHeight != 0) {
                setHeight(0);
                mHeight = 0;
            }
        } else if (mHeight != 40) {
            setHeight(40);
            mHeight = 40;
        }

        if (mStatus == Status.STATUS_ERROR || mStatus == Status.STATUS_OVER) {
            mProgressBar.setVisibility(GONE);
        } else {
            mProgressBar.setVisibility(VISIBLE);
        }
    }

    private void setHeight(int dpValue) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = MeasureUtils.dp2px(getContext(), dpValue);
        setLayoutParams(layoutParams);
    }

    @Override
    public void onRequestFail(HttpException e) {
        setStatus(Status.STATUS_ERROR);
    }

    @Override
    public void onStart() {
        if (mIndex == -1) {
            setStatus(Status.STATUS_OVER);
        } else {
            if (mStatus != Status.STATUS_OVER) {
                setStatus(Status.STATUS_LOAD);
            }
        }
    }

    @Override
    public void onRequestSuccess() {
        if (mStatus == Status.STATUS_LOAD) {
            setStatus(Status.STATUS_NORMAL);
        }
    }

    @Override
    public void onComplete() {
    }
}
