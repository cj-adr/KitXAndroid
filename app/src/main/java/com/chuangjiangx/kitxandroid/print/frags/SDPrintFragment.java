package com.chuangjiangx.kitxandroid.print.frags;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.chuangjiangx.print.PrintSupport;
import com.chuangjiangx.print.impl.sd.SdPrinter;
import com.chuangjiangx.print.size.Print80Size;

/**
 * 桑达打印
 */
public class SDPrintFragment extends BasePrintFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PrintSupport.getInstance().init(mContext, new SdPrinter(), new Print80Size());

        // TODO
    }

}
