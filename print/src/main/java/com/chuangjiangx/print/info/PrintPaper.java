package com.chuangjiangx.print.info;

import android.graphics.Bitmap;

import com.chuangjiangx.print.PrintLogUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 打印的内容
 */
public final class PrintPaper {

    private List<IPrintInfo> mList = new ArrayList<>();

    private PrintPaper() {
    }

    public List<IPrintInfo> getPrintList() {
        return mList;
    }

    public static class Builder {

        private PrintPaper mPrintPaper;

        public Builder() {
            mPrintPaper = new PrintPaper();
        }

        /**
         * 添加 IPrintInfo
         */
        public Builder add(IPrintInfo... params) {
            if (null == params || params.length <= 0) {
                return this;
            }

            Collections.addAll(mPrintPaper.mList, params);

            return this;
        }

        /**
         * 添加 IPrintInfo
         */
        public <T extends IPrintInfo> Builder add(List<T> params) {
            if (null == params || params.size() <= 0) {
                return this;
            }

            mPrintPaper.mList.addAll(params);

            return this;
        }

        /**
         * 添加文本
         */
        public Builder addTxt(String... txt) {
            return add(PrintTxtInfo.newList(txt));
        }

        /**
         * 添加两列文本
         */
        public Builder add2ColTxt(String s1, String s2) {
            return add(PrintTxtInfo.new2ColTxt(s1, s2));
        }

        /**
         * 添加三列文本
         */
        public Builder add3ColTxt(String s1, String s2, String s3) {
            return add(PrintTxtInfo.new3ColTxt(s1, s2, s3));
        }

        /**
         * 添加居中文本
         */
        public Builder addCenterTxt(String txt) {
            return add(PrintTxtInfo.newCenterTxt(txt));
        }

        /**
         * 添加居中文本
         */
        public Builder addCenterTxt(String txt, char c) {
            return add(PrintTxtInfo.newCenterTxt(txt, c));
        }

        /**
         * 添加标题，居中加粗
         */
        public Builder addTitle(String title) {
            return addTitle(title, ' ');
        }

        /**
         * 添加标题，居中加粗
         */
        public Builder addTitle(String title, char c) {
            return add(PrintTxtInfo.newTitle(title, c));
        }

        /**
         * 添加签名线 "签名：____________"
         */
        public Builder addMarkLine(String mark) {
            return add(PrintTxtInfo.newMarkLine(mark));
        }

        /**
         * 添加分割线 "------------"
         */
        public Builder addSplitLine() {
            return addSplitLine('-');
        }

        /**
         * 添加分割线
         */
        public Builder addSplitLine(char s) {
            return add(PrintTxtInfo.newSplitLine(s));
        }

        /**
         * 添加换行
         */
        public Builder addWrapLine() {
            return add(new PrintWrapInfo());
        }

        /**
         * 添加换行
         */
        public Builder addWrapLine(int count) {
            return add(new PrintWrapInfo(count));
        }

        /**
         * 添加一维码
         */
        public Builder addBarCode(String code) {
            return add(new PrintBarCodeInfo(code));
        }

        /**
         * 添加一维码
         */
        public Builder addBarCode(String code, int w, int h) {
            return add(new PrintBarCodeInfo(code, w, h));
        }

        /**
         * 添加二维码
         */
        public Builder addQrCode(String code) {
            return add(new PrintQrCodeInfo(code));
        }

        /**
         * 添加二维码
         */
        public Builder addQrCode(String code, int w) {
            return add(new PrintQrCodeInfo(code, w));
        }

        /**
         * 添加图片
         */
        public Builder addImg(Bitmap bitmap) {
            return add(new PrintImgInfo(bitmap));
        }

        /**
         * 添加图片
         */
        public Builder addImg(Bitmap bitmap, int n) {
            return add(new PrintImgInfo(bitmap, n));
        }

        /**
         * 复杂操作
         */
        public <Func extends Function> Builder merge(Func func) {
            func.handle(this);
            return this;
        }

        /**
         * Paper 准备完毕
         */
        public PrintPaper build() {
            // TODO 以后如果需要支持自定义格式，可以在此处进行格式解析
            PrintLogUtils.d("打印内容已经准备好！");

            return mPrintPaper;
        }
    }

    public interface Function {

        void handle(Builder it);
    }

}
