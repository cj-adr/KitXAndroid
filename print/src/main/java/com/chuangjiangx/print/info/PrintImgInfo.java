package com.chuangjiangx.print.info;

import android.graphics.Bitmap;

/**
 * 打印图片信息
 */
public class PrintImgInfo implements IPrintInfo {

    public Bitmap img; // 图片
    public int position; // 0左 1中 2右

    public PrintImgInfo(Bitmap img) {
        this(img, 1);
    }

    public PrintImgInfo(Bitmap img, int position) {
        this.img = img;

        if (position < 0 || position > 2) {
            this.position = 0;

        } else {
            this.position = position;
        }
    }

}
