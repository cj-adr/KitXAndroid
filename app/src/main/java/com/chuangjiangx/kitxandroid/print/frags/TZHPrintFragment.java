package com.chuangjiangx.kitxandroid.print.frags;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.chuangjiangx.print.PrintSupport;
import com.chuangjiangx.print.impl.tzh.TzhPrinter;
import com.chuangjiangx.print.size.Print80Size;

/**
 * 天之河打印
 */
public class TZHPrintFragment extends BasePrintFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PrintSupport.getInstance().init(mContext, new TzhPrinter(), new Print80Size());

        // TODO
    }

}
