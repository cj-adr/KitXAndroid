package com.chuangjiangx.kitxandroid.main;

import android.content.Intent;
import android.view.View;

import com.chuangjiangx.kitxandroid.IndexActivity;
import com.chuangjiangx.kitxandroid.network.index.NetworkActivity;
import com.chuangjiangx.kitxandroid.print.PrintTestActivity;
import com.chuangjiangx.kitxandroid.viewnavi.NaviActivity;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends IndexActivity {


    @Override
    public List<String> getItems() {
        return Arrays.asList("网络请求", "UI组建", "打印", "ViewNavi");
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        switch (position) {
            case 0:
                startActivity(new Intent(MainActivity.this, NetworkActivity.class));
                break;
            case 1:
                break;

            case 2:
                startActivity(new Intent(MainActivity.this, PrintTestActivity.class));
                break;

            case 3:
                startActivity(new Intent(MainActivity.this, NaviActivity.class));
                break;
        }
    }
}
