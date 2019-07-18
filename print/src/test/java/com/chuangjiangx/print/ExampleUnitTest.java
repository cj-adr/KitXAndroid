package com.chuangjiangx.print;

import com.chuangjiangx.print.info.PrintImgInfo;
import com.chuangjiangx.print.info.PrintInfo;
import com.chuangjiangx.print.info.PrintTxtInfo;
import com.chuangjiangx.print.info.PrintWrapInfo;
import com.chuangjiangx.print.size.Print80Size;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testPrint() {
        PrintSupport.getInstance().init(null, new TestPrinter(), new Print80Size());

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

        list.add(PrintInfo.newData(new PrintImgInfo(null)));

        list.add(PrintInfo.newData(new PrintWrapInfo(3)));

        PrintSupport.getInstance().print(list);


        PrintSupport.getInstance().close();
    }

}