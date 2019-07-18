package com.chuangjiangx.core.recylerview.loadmore;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chuangjiangx.core.network.itf.ListSourceInitialization;
import com.chuangjiangx.core.recylerview.ComplexAdapterBuilder;

import java.util.List;

public class LoadManage<T> {

    private LoadView mLoadView;

    private EmptyView mEmptyView;

    private RecyclerView mRecyclerView;

    private ComplexAdapterBuilder.ComplexAdapter mComplexAdapter;

    private EntityAdapter<T> mEntityAdapter;


    public static <T> LoadManage<T> newInstance(ListSourceInitialization listSourceInitialization, RecyclerView recyclerView, EntityAdapter<T> adapter) {
        return new LoadManage<>(recyclerView.getContext(), recyclerView, listSourceInitialization, adapter);
    }

    public LoadManage(Context context
            , RecyclerView recyclerView
            , ListSourceInitialization listSourceInitialization
            , EntityAdapter<T> adapter) {
        mRecyclerView = recyclerView;
        mLoadView = new LoadView(context, listSourceInitialization, false);
        mEmptyView = new EmptyView(context);
        mEntityAdapter = adapter;
        mComplexAdapter = new ComplexAdapterBuilder(adapter)
                .addFooter(mEmptyView)
                .addFooter(mLoadView).build();
        mComplexAdapter.setOnAutoLoadListener(mLoadView.getOnAutoLoadListener());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setAdapter(getAdapter());
    }


    public ComplexAdapterBuilder.ComplexAdapter getAdapter() {
        return mComplexAdapter;
    }

    public EntityAdapter<T> getEntityAdapter() {
        return mEntityAdapter;
    }

    public void refresh() {
        mComplexAdapter.notifyDataSetChanged();
    }

    public void refresh(int index) {
        mComplexAdapter.notifyDataSetChanged();
        if (index == 1) {
            mRecyclerView.scrollToPosition(0);
        }

    }

    public void refresh(int index, List<T> data) {
        getEntityAdapter().attachBeen(data, index);
        mComplexAdapter.notifyDataSetChanged();
        if (index == 1) {
            mRecyclerView.scrollToPosition(0);
        }

    }

    public LoadView getLoadView() {
        return mLoadView;
    }

    public EmptyView getEmptyView() {
        return mEmptyView;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }
}
