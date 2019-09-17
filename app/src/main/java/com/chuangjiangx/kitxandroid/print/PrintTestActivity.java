package com.chuangjiangx.kitxandroid.print;

import android.content.Intent;
import android.view.View;

import com.chuangjiangx.kitxandroid.IndexActivity;

import java.util.Arrays;
import java.util.List;

/**
 * 打印测试
 */
public class PrintTestActivity extends IndexActivity {

    @Override
    public List<String> getItems() {
        return Arrays.asList("USB打印", "商米T2", "蓝牙打印", "桑达打印", "拉卡拉打印");
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();

        Intent intent = new Intent(this, PrintActivity.class);
        intent.putExtra("type", position);
        startActivity(intent);
    }

}
