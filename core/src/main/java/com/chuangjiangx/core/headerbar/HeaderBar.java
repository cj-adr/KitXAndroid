package com.chuangjiangx.core.headerbar;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.chuangjiangx.core.R;

/**
 * Author: com.ysq
 * Time:2017/11/14 14:40
 */

public class HeaderBar extends FrameLayout implements View.OnClickListener {

    private String mNavText, mActionText, mTitleText;
    private Drawable mNavIcon, mActionIcon;
    private ColorStateList mTitleTextColor, mNavTextColor, mActionTextColor;
    private boolean mDivideEnable = true;
    private TextView mTvNav, mTvTitle, mTvAction;
    private View mViewDivide, mViewFocus;

    public HeaderBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initCustomAttrs(context, attrs);
        initView();
    }


    public void initCustomAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HeaderBar);
        final int count = typedArray.getIndexCount();
        for (int i = 0; i < count; i++) {
            initCustomAttr(typedArray.getIndex(i), typedArray);
        }
        typedArray.recycle();
    }


    private void initCustomAttr(int attr, TypedArray typedArray) {
        if (attr == R.styleable.HeaderBar_header_title) {
            mTitleText = typedArray.getString(attr);
        } else if (attr == R.styleable.HeaderBar_header_navIcon) {
            mNavIcon = typedArray.getDrawable(attr);
        } else if (attr == R.styleable.HeaderBar_header_actionIcon) {
            mActionIcon = typedArray.getDrawable(attr);
        } else if (attr == R.styleable.HeaderBar_header_navText) {
            mNavText = typedArray.getString(attr);
        } else if (attr == R.styleable.HeaderBar_header_actionText) {
            mActionText = typedArray.getString(attr);
        } else if (attr == R.styleable.HeaderBar_header_titleTextColor) {
            mTitleTextColor = typedArray.getColorStateList(attr);
        } else if (attr == R.styleable.HeaderBar_header_navTextColor) {
            mNavTextColor = typedArray.getColorStateList(attr);
        } else if (attr == R.styleable.HeaderBar_header_actionTextColor) {
            mActionTextColor = typedArray.getColorStateList(attr);
        } else if (attr == R.styleable.HeaderBar_header_divide_enable) {
            mDivideEnable = typedArray.getBoolean(attr, true);
        }
    }

    private void initView() {
        View contentView = LayoutInflater.from(getContext()).inflate(
                R.layout.kitx_layout_headerbar, this, false);
        addView(contentView, new LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        bindView();
        initFocus();
        initNavigation();
        initTitle();
        initAction();
        initDivide();
    }

    private void bindView() {
        mTvNav = (TextView) findViewById(R.id.header_nav);
        mTvTitle = (TextView) findViewById(R.id.header_title);
        mTvAction = (TextView) findViewById(R.id.header_action);
        mViewDivide = findViewById(R.id.view_divide);
        mViewFocus = findViewById(R.id.view_focus);
    }

    private void initFocus() {
        mViewFocus.requestFocus();
    }

    private void initNavigation() {
        if (!TextUtils.isEmpty(mNavText)) {
            mTvNav.setText(mNavText);
        }
        if (mNavIcon != null) {
            mTvNav.setCompoundDrawablesWithIntrinsicBounds(mNavIcon, null, null, null);
        }
        if (mNavTextColor != null) {
            mTvNav.setTextColor(mNavTextColor);
        }
        if (!TextUtils.isEmpty(mNavText) || mNavIcon != null) {
            mTvNav.setOnClickListener(this);
        }
    }

    private void initTitle() {
        if (!TextUtils.isEmpty(mTitleText)) {
            mTvTitle.setText(mTitleText);
        }
        if (mTitleTextColor != null) {
            mTvTitle.setTextColor(mTitleTextColor);
        }
    }

    private void initAction() {
        if (!TextUtils.isEmpty(mActionText)) {
            mTvAction.setText(mActionText);
        }
        if (mActionIcon != null) {
            mTvAction.setCompoundDrawablesWithIntrinsicBounds(null, null, mActionIcon, null);
        }
        if (mActionTextColor != null) {
            mTvAction.setTextColor(mActionTextColor);
        }
    }

    private void initDivide() {
        mViewDivide.setVisibility(mDivideEnable ? VISIBLE : GONE);
    }

    public void setNavClick(OnClickListener clickListener) {
        mTvNav.setOnClickListener(clickListener);
    }

    public void setActionClick(OnClickListener clickListener) {
        mTvAction.setOnClickListener(clickListener);
    }

    public void setActionVisible(int visible) {
        mTvAction.setVisibility(visible);
    }

    public void setTitle(String title) {
        mTitleText = title;
        mTvTitle.setText(title);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.header_nav) {
            if (getContext() instanceof Activity)
                ((Activity) getContext()).finish();
        }
    }


    public TextView getTvAction() {
        return mTvAction;
    }
}