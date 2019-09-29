package com.chuangjiangx.kitxandroid.print.frags;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.chuangjiangx.kitxandroid.R;
import com.chuangjiangx.kitxandroid.network.base.BaseFragment;
import com.chuangjiangx.print.PrintSupport;
import com.chuangjiangx.print.info.PrintPaper;

public abstract class BasePrintFragment extends BaseFragment {

    Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    void print() {
        PrintPaper.Builder builder = new PrintPaper.Builder();

        PrintPaper paper = builder.addTitle("创匠科技", '*')
                .addSplitLine()
                .addTxt("门店名称：创匠美发馆",
                        "收 营 员：张三",
                        "订 单 号：2019032312345677886555",
                        "支付方式：会员卡",
                        "支付状态：支付成功",
                        "支付时间：2019年1月2日 18:00:01")
                .addSplitLine()

                .add2ColTxt("项目", "数量")
                .addSplitLine()
                .add2ColTxt("洗剪吹", "2次")
                .addSplitLine()

                .add3ColTxt("项目", "数量", "价格")
                .addSplitLine()

                .add3ColTxt("洗剪吹", "2次", "189.00")
                .add3ColTxt("背部护理", "1次", "289.00")
                .addSplitLine()

                .addTxt("顾客扣款：")
                .addTitle("RMB：0.01")
                .addWrapLine()
                .addMarkLine("签    名")

                .addWrapLine()
                .addImg(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .addWrapLine(4)
                .addBarCode("barcode111111")
                .addWrapLine()
                .addQrCode("qrcode111111")
                .addWrapLine()
                .merge(new Merge())
                .merge(new Merge2(), "哈哈哈")

                .addCenterTxt("请保存好发票！")
                .build();

        new Thread(() -> PrintSupport.getInstance().print(paper)).start();
    }

    private class Merge implements PrintPaper.Function {

        @Override
        public void handle(PrintPaper.Builder it) {

            it.addTxt("test");
        }
    }

    private class Merge2 implements PrintPaper.Function2<String> {

        @Override
        public void handle(PrintPaper.Builder it, String s) {

            it.addTxt("test: " + s);
        }
    }

    @Override
    public void onDestroy() {
        PrintSupport.getInstance().close();
        super.onDestroy();
    }

}
