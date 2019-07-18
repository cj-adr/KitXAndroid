package com.chuangjiangx.kitxandroid.network.index;

import android.content.Intent;
import android.view.View;

import com.chuangjiangx.kitxandroid.IndexActivity;
import com.chuangjiangx.kitxandroid.network.demo1.NetworkDemo1Activity;
import com.chuangjiangx.kitxandroid.network.demo2.NetworkDemo2Activity;
import com.chuangjiangx.kitxandroid.network.demo3.NetworkDemo3Activity;

import java.util.Arrays;
import java.util.List;

public class NetworkActivity extends IndexActivity {

    @Override
    public List<String> getItems() {
        return Arrays.asList("初始化数据请求，无刷新", "初始化数据请求，含刷新", "列表请求，含下拉刷新上拉加载");
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        switch (position) {
            case 0:
                startActivity(new Intent(NetworkActivity.this, NetworkDemo1Activity.class));
                break;
            case 1:
                startActivity(new Intent(NetworkActivity.this, NetworkDemo2Activity.class));
                break;
            case 2:
                startActivity(new Intent(NetworkActivity.this, NetworkDemo3Activity.class));
                break;
        }
    }
}
