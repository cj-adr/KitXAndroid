package com.chuangjiangx.kitxandroid.network.demo3;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chuangjiangx.core.recylerview.loadmore.EntityAdapter;
import com.chuangjiangx.kitxandroid.R;

public class Demo3Adapter extends EntityAdapter<String> {

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_network_demo3, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((TextView) ((VH) holder).itemView).setText(mBeen.get(position));
    }

    @Override
    public int getItemCount() {
        return mBeen.size();
    }

    static class VH extends RecyclerView.ViewHolder {

        public VH(@NonNull View itemView) {
            super(itemView);
        }
    }
}
