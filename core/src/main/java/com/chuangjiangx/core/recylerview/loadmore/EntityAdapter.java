package com.chuangjiangx.core.recylerview.loadmore;


import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: yangshuiqiang
 * Time:2018/1/9
 */
public abstract class EntityAdapter<T> extends RecyclerView.Adapter {
    protected List<T> mBeen = new ArrayList<>();

    public void addBeen(List<T> been) {
        if (been != null)
            this.mBeen.addAll(been);
    }

    public void setBeen(List<T> been) {
        if (been == null) been = new ArrayList<>();
        this.mBeen = been;
    }

    public void attachBeen(List<T> been, int page) {
        if (1 == page) {
            setBeen(been);
        } else {
            addBeen(been);
        }
    }

    public List<T> getBeen() {
        return mBeen;
    }

}
