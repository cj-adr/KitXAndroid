package com.chuangjiangx.kitxandroid.print.frags;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.chuangjiangx.print.PrintSupport;
import com.chuangjiangx.print.impl.lacara.LacaraPrinter;
import com.chuangjiangx.print.size.Print80Size;

public class LklPrintFragment extends BasePrintFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PrintSupport.getInstance().init(mContext, new LacaraPrinter(), new Print80Size());
    }

}
