package com.chuangjiangx.print.info;

import android.graphics.Bitmap;

/**
 * 打印图片信息
 */
public class PrintImgInfo implements IPrintInfo {

    public Bitmap img;

    public PrintImgInfo(Bitmap img) {
        this.img = img;
    }

}