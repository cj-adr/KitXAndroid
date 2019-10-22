package com.chuangjiangx.core.loading;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.chuangjiangx.core.R;


public class ProgressDialog {

    private PopupWindow mWindow;
    private View mContentView;

    public ProgressDialog(Context context, View contentView) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.kitx_layout_progress, null);
        mWindow = new PopupWindow(inflate, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置PopupWindow的背景
//                window.setBackgroundDrawable(new ColorDrawable(Color.RED));
        // 设置PopupWindow是否能响应外部点击事件
        mWindow.setOutsideTouchable(true);
        // 设置PopupWindow是否能响应点击事件
        mWindow.setTouchable(true);
        // 显示PopupWindow，其中：
        // 第一个参数是PopupWindow的锚点，第二和第三个参数分别是PopupWindow相对锚点的x、y偏移
        mContentView = contentView;
    }

    public void show() {
        mWindow.showAtLocation(mContentView, Gravity.CENTER, 0, 0);
    }

    public void hide() {
        if (mWindow != null && mWindow.isShowing())
            mWindow.dismiss();
    }

}
