package com.chuangjiangx.kitxandroid.print.frags;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chuangjiangx.kitxandroid.R;
import com.chuangjiangx.print.PrintSupport;
import com.chuangjiangx.print.impl.usb.UsbPrinter;
import com.chuangjiangx.print.size.Print58Size;

/**
 * USB打印
 */
public class UsbPrintFragment extends BasePrintFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PrintSupport.getInstance().init(mContext, new UsbPrinter(), new Print58Size());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_print, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btn_print).setOnClickListener(v -> {
            if (PrintSupport.getInstance().checkPrintable()) {
                print();
                return;
            }

            Toast.makeText(mContext, "请连接USB打印机！", Toast.LENGTH_SHORT).show();

        });
    }

}
