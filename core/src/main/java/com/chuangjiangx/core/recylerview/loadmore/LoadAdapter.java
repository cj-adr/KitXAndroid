package com.chuangjiangx.core.recylerview.loadmore;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

/**
 * @author: yangshuiqiang
 * Time:2017/12/31
 */

public abstract class LoadAdapter extends RecyclerView.Adapter {

    private OnAutoLoadListener mOnAutoLoadListener;

    @Override
    public void onAttachedToRecyclerView(@NonNull final RecyclerView recyclerView) {
        if (mOnAutoLoadListener != null) {
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    shouldLoad(recyclerView);
                }

                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    shouldLoad(recyclerView);
                }
            });
        }
    }

    private void shouldLoad(RecyclerView recyclerView) {
        if (((LinearLayoutManager) Objects.requireNonNull(recyclerView.getLayoutManager()))
                .findLastVisibleItemPosition() >= getItemCount() - 5
                && getItemCount() >= Page.EVERY_PAGE_COUNT) {
            if (mOnAutoLoadListener != null) {
                mOnAutoLoadListener.autoLoad();
            }
        }
    }

    public void setOnAutoLoadListener(OnAutoLoadListener onAutoLoadListener) {
        mOnAutoLoadListener = onAutoLoadListener;
    }


    interface OnAutoLoadListener {
        void autoLoad();
    }
}