package com.chuangjiangx.kitxandroid.print.frags;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.chuangjiangx.kitxandroid.R;
import com.chuangjiangx.kitxandroid.network.base.BaseFragment;
import com.chuangjiangx.print.PrintSupport;
import com.chuangjiangx.print.info.IPrintInfo;
import com.chuangjiangx.print.info.PrintImgInfo;
import com.chuangjiangx.print.info.PrintTxtInfo;
import com.chuangjiangx.print.info.PrintWrapInfo;

import java.util.ArrayList;
import java.util.List;

public abstract class BasePrintFragment extends BaseFragment {

    Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    void print() {
        List<IPrintInfo> list = new ArrayList<>();

        list.add(PrintTxtInfo.newTitle(PrintSupport.getInstance().getPrintSize(), "创匠科技", '*'));

        list.add(PrintTxtInfo.newNormalTxt(PrintSupport.getInstance().getPrintSize().getLine('-')));

        list.add(PrintTxtInfo.newNormalTxt("门店名称：创匠美发馆"));
        list.add(PrintTxtInfo.newNormalTxt("收 营 员：张三"));
        list.add(PrintTxtInfo.newNormalTxt("订 单 号：2019032312345677886555"));
        list.add(PrintTxtInfo.newNormalTxt("支付方式：会员卡"));
        list.add(PrintTxtInfo.newNormalTxt("支付状态：支付成功"));
        list.add(PrintTxtInfo.newNormalTxt("支付时间：2019年1月2日 18:00:01"));

        list.add(PrintTxtInfo.newNormalTxt(PrintSupport.getInstance().getPrintSize().getLine('-')));

        list.add(PrintTxtInfo.new2ColTxt(PrintSupport.getInstance().getPrintSize(), "项目", "数量"));
        list.add(PrintTxtInfo.newNormalTxt(PrintSupport.getInstance().getPrintSize().getLine('-')));
        list.add(PrintTxtInfo.new2ColTxt(PrintSupport.getInstance().getPrintSize(), "洗剪吹", "2次"));

        list.add(PrintTxtInfo.newNormalTxt(PrintSupport.getInstance().getPrintSize().getLine('-')));

        list.add(PrintTxtInfo.new3ColTxt(PrintSupport.getInstance().getPrintSize(), "项目", "数量", "价格"));
        list.add(PrintTxtInfo.newNormalTxt(PrintSupport.getInstance().getPrintSize().getLine('-')));
        list.add(PrintTxtInfo.new3ColTxt(PrintSupport.getInstance().getPrintSize(), "洗剪吹", "2次", "189.00"));
        list.add(PrintTxtInfo.new3ColTxt(PrintSupport.getInstance().getPrintSize(), "背部护理", "1次", "289.00"));

        list.add(PrintTxtInfo.newNormalTxt(PrintSupport.getInstance().getPrintSize().getLine('-')));

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        list.add(new PrintImgInfo(bitmap));

        list.add(new PrintWrapInfo(1));

        new Thread(() -> PrintSupport.getInstance().print(list)).start();
    }

    @Override
    public void onDestroy() {
        PrintSupport.getInstance().close();
        super.onDestroy();
    }

}
