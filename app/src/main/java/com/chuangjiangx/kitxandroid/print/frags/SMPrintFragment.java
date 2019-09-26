package com.chuangjiangx.kitxandroid.print.frags;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chuangjiangx.kitxandroid.R;
import com.chuangjiangx.print.PrintLogUtils;
import com.chuangjiangx.print.PrintSupport;
import com.chuangjiangx.print.impl.bluetooth.BluetoothPrinter;
import com.chuangjiangx.print.impl.sunmisc.SmPrinter;
import com.chuangjiangx.print.size.Print80Size;

/**
 * 商米T2打印
 */
public class SMPrintFragment extends BasePrintFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PrintSupport.getInstance().init(mContext, new SmPrinter(), new Print80Size());

        // 使用内置虚拟蓝牙打印(经测试无法打印，官方demo也无法打印)
//        PrintSupport.getInstance().init(mContext, new BluetoothPrinter("00:11:22:33:44:55", getBluetoothConnectListener()), new Print80Size());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_print, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btn_print).setOnClickListener(v -> print());
    }

    /**
     * 蓝牙连接监听
     */
    private BluetoothPrinter.BluetoothConnectListener getBluetoothConnectListener() {
        return new BluetoothPrinter.BluetoothConnectListener() {

            @Override
            public void onConnectSuccess(String address) {
                PrintLogUtils.d("连接成功：" + address);
            }

            @Override
            public void onConnectFail(String address, Throwable e) {
                PrintLogUtils.d("连接失败：" + address);
            }
        };
    }

}
