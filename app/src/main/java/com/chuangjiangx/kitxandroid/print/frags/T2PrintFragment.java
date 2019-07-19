package com.chuangjiangx.kitxandroid.print.frags;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chuangjiangx.kitxandroid.R;
import com.chuangjiangx.print.PrintSupport;
import com.chuangjiangx.print.impl.sunmisc.t2.T2Printer;
import com.chuangjiangx.print.size.Print80Size;

/**
 * 商米T2打印
 */
public class T2PrintFragment extends BasePrintFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PrintSupport.getInstance().init(mContext, new T2Printer(), new Print80Size());
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
}
