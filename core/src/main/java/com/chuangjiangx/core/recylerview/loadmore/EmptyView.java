package com.chuangjiangx.core.recylerview.loadmore;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.chuangjiangx.core.R;
import com.chuangjiangx.core.utils.MeasureUtils;

/**
 * @author: yangshuiqiang
 * Time:2017/11/26 20:46
 */

@SuppressLint("ViewConstructor")
public class EmptyView extends FrameLayout {

    private int mHeight = -1;

    private int mShowHeight;

    public EmptyView(@NonNull Context context) {
        super(context);
        mShowHeight = MeasureUtils.dp2px(getContext(), 200);
        initView(context);
    }

    private void initView(Context context) {
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mShowHeight));
        View inflate = LayoutInflater.from(context).inflate(R.layout.kitx_layout_empty, this, false);
        LayoutParams layoutParams =
                new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER;
        addView(inflate, layoutParams);
    }

    public void setVisible(int count, int height) {
        if (height != 0) {
            mShowHeight = height;
        }
        if (count > 0) {
            hide();
        } else {
            show();
        }
    }


    private void show() {
        if (mHeight != mShowHeight) {
            setHeight(mShowHeight);
            mHeight = mShowHeight;
        }
    }

    private void hide() {
        if (mHeight != 0) {
            setHeight(0);
            mHeight = 0;
        }
    }

    private void setHeight(int dpValue) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = dpValue;
        setLayoutParams(layoutParams);
    }
}
