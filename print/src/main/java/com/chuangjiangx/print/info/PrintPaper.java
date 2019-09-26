package com.chuangjiangx.print.info;

import android.graphics.Bitmap;

import com.chuangjiangx.print.PrintLogUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 打印的内容
 */
public class PrintPaper {

    private List<IPrintInfo> mList = new ArrayList<>();

    public List<IPrintInfo> getPrintList() {
        return mList;
    }

    /**
     * 添加 IPrintInfo
     */
    public PrintPaper add(IPrintInfo... params) {
        if (null == params || params.length <= 0) {
            return this;
        }

        Collections.addAll(mList, params);

        return this;
    }

    /**
     * 添加 IPrintInfo
     */
    public <T extends IPrintInfo> PrintPaper add(List<T> params) {
        if (null == params || params.size() <= 0) {
            return this;
        }

        mList.addAll(params);

        return this;
    }

    /**
     * 添加文本
     */
    public PrintPaper addTxt(String... txt) {
        add(PrintTxtInfo.newList(txt));
        return this;
    }

    /**
     * 添加两列文本
     */
    public PrintPaper add2ColTxt(String s1, String s2) {
        add(PrintTxtInfo.new2ColTxt(s1, s2));
        return this;
    }

    /**
     * 添加三列文本
     */
    public PrintPaper add3ColTxt(String s1, String s2, String s3) {
        add(PrintTxtInfo.new3ColTxt(s1, s2, s3));
        return this;
    }

    /**
     * 添加居中文本
     */
    public PrintPaper addCenterTxt(String txt) {
        add(PrintTxtInfo.newCenterTxt(txt));
        return this;
    }

    /**
     * 添加标题，居中加粗
     */
    public PrintPaper addTitle(String title) {
        return addTitle(title, ' ');
    }

    /**
     * 添加标题，居中加粗
     */
    public PrintPaper addTitle(String title, char c) {
        add(PrintTxtInfo.newTitle(title, c));
        return this;
    }

    /**
     * 添加签名线 "签名：____________"
     */
    public PrintPaper addMarkLine(String mark) {
        add(PrintTxtInfo.newMarkLine(mark));
        return this;
    }

    /**
     * 添加分割线 "------------"
     */
    public PrintPaper addSplitLine() {
        return addSplitLine('-');
    }

    /**
     * 添加分割线
     */
    public PrintPaper addSplitLine(char s) {
        add(PrintTxtInfo.newSplitLine(s));
        return this;
    }

    /**
     * 添加换行
     */
    public PrintPaper addWrapLine() {
        add(new PrintWrapInfo());
        return this;
    }

    /**
     * 添加换行
     */
    public PrintPaper addWrapLine(int count) {
        add(new PrintWrapInfo(count));
        return this;
    }

    /**
     * 添加一维码
     */
    public PrintPaper addBarCode(String code) {
        add(new PrintBarCodeInfo(code));
        return this;
    }

    /**
     * 添加一维码
     */
    public PrintPaper addBarCode(String code, int w, int h) {
        add(new PrintBarCodeInfo(code, w, h));
        return this;
    }

    /**
     * 添加二维码
     */
    public PrintPaper addQrCode(String code) {
        add(new PrintQrCodeInfo(code));
        return this;
    }

    /**
     * 添加二维码
     */
    public PrintPaper addQrCode(String code, int w) {
        add(new PrintQrCodeInfo(code, w));
        return this;
    }

    /**
     * 添加图片
     */
    public PrintPaper addImg(Bitmap bitmap) {
        add(new PrintImgInfo(bitmap));
        return this;
    }

    /**
     * 添加图片
     */
    public PrintPaper addImg(Bitmap bitmap, int n) {
        add(new PrintImgInfo(bitmap, n));
        return this;
    }

    /**
     * Paper 准备完毕
     */
    public void finish() {
        // TODO 以后如果需要支持自定义格式，可以在此处进行格式解析
        PrintLogUtils.d("打印内容已经准备好！");
    }

}
