package com.chuangjiangx.kitxandroid.print;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.View;

import com.chuangjiangx.kitxandroid.IndexActivity;
import com.chuangjiangx.kitxandroid.R;
import com.chuangjiangx.print.PrintSupport;
import com.chuangjiangx.print.impl.bluetooth.BluetoothPrinter;
import com.chuangjiangx.print.impl.lacara.LacaraPrinter;
import com.chuangjiangx.print.impl.sd.SdPrinter;
import com.chuangjiangx.print.impl.sunmisc.d2.D2Printer;
import com.chuangjiangx.print.impl.sunmisc.t2.T2Printer;
import com.chuangjiangx.print.impl.tzh.TzhPrinter;
import com.chuangjiangx.print.info.PrintImgInfo;
import com.chuangjiangx.print.info.PrintInfo;
import com.chuangjiangx.print.info.PrintTxtInfo;
import com.chuangjiangx.print.info.PrintWrapInfo;
import com.chuangjiangx.print.size.Print58Size;
import com.chuangjiangx.print.size.Print80Size;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 打印测试
 */
public class PrintTestActivity extends IndexActivity {

    private Handler mHandler = new Handler();

    @Override
    public List<String> getItems() {
        return Arrays.asList("商米D2", "商米T2", "蓝牙打印", "桑达打印", "天之河打印", "拉卡拉打印");
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        switch (position) {
            case 0:
                d2Print();
                break;

            case 1:
                t2Print();
                break;

            case 2:
                bluetoothPrint();
                break;

            case 3:
                sdPrint();
                break;

            case 4:
                tzhPrint();
                break;

            case 5:
                lklPrint();
                break;
        }
    }

    private void d2Print() {
        PrintSupport.getInstance().init(getApplicationContext(), new D2Printer(), new Print58Size());
        print();
    }

    private void t2Print() {
        PrintSupport.getInstance().init(getApplicationContext(), new T2Printer(), new Print80Size());
        print();
    }

    private void bluetoothPrint() {
        PrintSupport.getInstance().init(getApplicationContext(), new BluetoothPrinter(), new Print80Size());
        print();
    }

    private void sdPrint() {
        PrintSupport.getInstance().init(getApplicationContext(), new SdPrinter(), new Print80Size());
        print();
    }

    private void tzhPrint() {
        PrintSupport.getInstance().init(getApplicationContext(), new TzhPrinter(), new Print80Size());
        print();
    }

    private void lklPrint() {
        PrintSupport.getInstance().init(getApplicationContext(), new LacaraPrinter(), new Print80Size());
        print();
    }

    private void print() {
        List<PrintInfo> list = new ArrayList<>();

        list.add(PrintInfo.newData(PrintTxtInfo.newTitle(PrintSupport.getInstance().getPrintSize(), "创匠科技", '*')));

        list.add(PrintInfo.newData(new PrintWrapInfo(1)));
        list.add(PrintInfo.newData(PrintTxtInfo.newNormalTxt(PrintSupport.getInstance().getPrintSize().getLine('-'))));

        list.add(PrintInfo.newData(PrintTxtInfo.newNormalTxt("门店名称：创匠美发馆")));
        list.add(PrintInfo.newData(PrintTxtInfo.newNormalTxt("收 营 员：张三")));
        list.add(PrintInfo.newData(PrintTxtInfo.newNormalTxt("订 单 号：2019032312345677886555")));
        list.add(PrintInfo.newData(PrintTxtInfo.newNormalTxt("支付方式：会员卡")));
        list.add(PrintInfo.newData(PrintTxtInfo.newNormalTxt("支付状态：支付成功")));
        list.add(PrintInfo.newData(PrintTxtInfo.newNormalTxt("支付时间：2019年1月2日 18:00:01")));

        list.add(PrintInfo.newData(new PrintWrapInfo(1)));
        list.add(PrintInfo.newData(PrintTxtInfo.newNormalTxt(PrintSupport.getInstance().getPrintSize().getLine('-'))));

        list.add(PrintInfo.newData(PrintTxtInfo.new2ColTxt(PrintSupport.getInstance().getPrintSize(), "项目", "数量")));
        list.add(PrintInfo.newData(PrintTxtInfo.newNormalTxt(PrintSupport.getInstance().getPrintSize().getLine('-'))));
        list.add(PrintInfo.newData(PrintTxtInfo.new2ColTxt(PrintSupport.getInstance().getPrintSize(), "洗剪吹", "2次")));

        list.add(PrintInfo.newData(new PrintWrapInfo(1)));
        list.add(PrintInfo.newData(PrintTxtInfo.newNormalTxt(PrintSupport.getInstance().getPrintSize().getLine('-'))));

        list.add(PrintInfo.newData(PrintTxtInfo.new3ColTxt(PrintSupport.getInstance().getPrintSize(), "项目", "数量", "价格")));
        list.add(PrintInfo.newData(PrintTxtInfo.newNormalTxt(PrintSupport.getInstance().getPrintSize().getLine('-'))));
        list.add(PrintInfo.newData(PrintTxtInfo.new3ColTxt(PrintSupport.getInstance().getPrintSize(), "洗剪吹", "2次", "189.00")));
        list.add(PrintInfo.newData(PrintTxtInfo.new3ColTxt(PrintSupport.getInstance().getPrintSize(), "背部护理", "1次", "289.00")));

        list.add(PrintInfo.newData(new PrintWrapInfo(1)));
        list.add(PrintInfo.newData(PrintTxtInfo.newNormalTxt(PrintSupport.getInstance().getPrintSize().getLine('-'))));

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        list.add(PrintInfo.newData(new PrintImgInfo(bitmap)));

        list.add(PrintInfo.newData(new PrintWrapInfo(3)));

        mHandler.postDelayed(() -> new Thread(() ->
                        PrintSupport.getInstance().print(list)).start(),
                2000
        );
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        PrintSupport.getInstance().close();
        super.onDestroy();
    }

}
