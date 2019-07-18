package com.chuangjiangx.core.recylerview;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chuangjiangx.core.recylerview.loadmore.LoadAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: com.ysq
 * Time:2017/11/8 18:44
 */

public class ComplexAdapterBuilder {

    private static final int BASE_TYPE_HEADER = -10000;
    private static final int BASE_TYPE_FOOTER = -20000;

    private RecyclerView.Adapter mAdapter;
    private List<View> mHeaderViews;
    private List<View> mFooterViews;

    public ComplexAdapterBuilder(RecyclerView.Adapter adapter) {
        mAdapter = adapter;
        mHeaderViews = new ArrayList<>();
        mFooterViews = new ArrayList<>();
    }

    public ComplexAdapterBuilder addHeader(View headerView) {
        mHeaderViews.add(headerView);
        return this;
    }

    public ComplexAdapterBuilder addFooter(View footerView) {
        mFooterViews.add(footerView);
        return this;
    }


    public ComplexAdapter build() {
        return new ComplexAdapter();
    }

    public class ComplexAdapter extends LoadAdapter {

        public RecyclerView mRecyclerView;

        private ComplexAdapter() {
        }

        @Override
        public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            mRecyclerView = recyclerView;
        }

        @Override
        public int getItemViewType(int position) {
            if (position < mHeaderViews.size()) {
                return BASE_TYPE_HEADER - position;
            } else if (position < mAdapter.getItemCount() + mHeaderViews.size()) {
                int type = mAdapter.getItemViewType(position - mHeaderViews.size());
                if (type <= BASE_TYPE_HEADER) {
                    throw new RuntimeException("the return of the method getItemViewType of adapter must be greater than -1000");
                } else {
                    return type;
                }
            } else {
                return BASE_TYPE_FOOTER - (position - mAdapter.getItemCount() - mHeaderViews.size());
            }
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType <= BASE_TYPE_HEADER && viewType > BASE_TYPE_FOOTER) {
                return new RecyclerView.ViewHolder(mHeaderViews.get(BASE_TYPE_HEADER - viewType)) {
                };
            } else if (viewType <= BASE_TYPE_FOOTER) {
                return new RecyclerView.ViewHolder(mFooterViews.get(BASE_TYPE_FOOTER - viewType)) {
                };
            } else {
                return mAdapter.onCreateViewHolder(parent, viewType);
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (position >= mHeaderViews.size() && position < mAdapter.getItemCount() + mHeaderViews.size()) {
                mAdapter.onBindViewHolder(holder, position - mHeaderViews.size());
            }
        }

        @Override
        public int getItemCount() {
            return mHeaderViews.size() + mAdapter.getItemCount() + mFooterViews.size();
        }

        public int getDisplayCount() {
            return mAdapter.getItemCount();
        }
    }
}