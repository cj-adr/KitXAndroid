package com.chuangjiangx.kitxandroid.print;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.chuangjiangx.core.network.itf.SourceInitialization;
import com.chuangjiangx.kitxandroid.R;
import com.chuangjiangx.kitxandroid.network.base.BaseActivity;
import com.chuangjiangx.kitxandroid.print.frags.BluetoothPrintFragment;
import com.chuangjiangx.kitxandroid.print.frags.LklPrintFragment;
import com.chuangjiangx.kitxandroid.print.frags.SDPrintFragment;
import com.chuangjiangx.kitxandroid.print.frags.T2PrintFragment;
import com.chuangjiangx.kitxandroid.print.frags.UsbPrintFragment;

/**
 * 打印界面
 */
public class PrintActivity extends BaseActivity implements SourceInitialization {

    private int type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);

        type = getIntent().getIntExtra("type", 0);

        sourceInit();
    }

    @Override
    public void sourceInit() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_container, getFragment())
                .commit();
    }

    private Fragment getFragment() {
        switch (type) {
            case 1:
                return new T2PrintFragment();

            case 2:
                return new BluetoothPrintFragment();

            case 3:
                return new SDPrintFragment();

            case 4:
                return new LklPrintFragment();

            default:
                return new UsbPrintFragment();
        }
    }

}
