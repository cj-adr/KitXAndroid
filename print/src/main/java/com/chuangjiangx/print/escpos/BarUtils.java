package com.chuangjiangx.print.escpos;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.HashMap;
import java.util.Map;

/**
 * 二维码/条形码转换工具
 */
public class BarUtils {

    /**
     * 将文本转成bitmap
     */
    public static Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int desiredWidth, int desiredHeight) {
        return encodeAsBitmapOffset(contents, format, desiredWidth, desiredHeight, 0);
    }

    /**
     * 将文本转成bitmap
     */
    public static Bitmap encodeAsBitmapOffset(String contents, BarcodeFormat format, int desiredWidth, int desiredHeight, int offsetX) {
        try {
            final int WHITE = 0xFFFFFFFF;
            final int BLACK = 0xFF000000;
            MultiFormatWriter writer = new MultiFormatWriter();
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.MARGIN, 0);
            BitMatrix result = writer.encode(contents, format, desiredWidth, desiredHeight, hints);

            int width = result.getWidth();
            int height = result.getHeight();
            int[] pixels = new int[width * height];
            // All are 0, or black, by default
            for (int y = 0; y < height; y++) {
                int offset = y * width;
                for (int x = 0; x < width; x++) {
                    pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, offsetX, 0, width, height);
            return bitmap;

        } catch (WriterException ignored) {
        }

        return null;
    }

    public static byte[] getBitmapPrintData(Bitmap bmp) {
        //用来存储转换后的 bitmap 数据。为什么要再加1000，这是为了应对当图片高度无法
        //整除24时的情况。比如bitmap 分辨率为 240 * 250，占用 7500 byte，
        //但是实际上要存储11行数据，每一行需要 24 * 240 / 8 =720byte 的空间。再加上一些指令存储的开销，
        //所以多申请 1000byte 的空间是稳妥的，不然运行时会抛出数组访问越界的异常。
        int size = bmp.getWidth() * bmp.getHeight() / 8 + 1000;
        byte[] data = new byte[size];
        int k = 0;
        //设置行距为30的指令
        data[k++] = 0x1B;
        data[k++] = 0x33;
        data[k++] = 0x30;
        // 逐行打印
        for (int j = 0; j < bmp.getHeight() / 24f; j++) {
            //打印图片的指令
            data[k++] = 0x1B;
            data[k++] = 0x2A;
            data[k++] = 33;
            data[k++] = (byte) (bmp.getWidth() % 256); //nL
            data[k++] = (byte) (bmp.getWidth() / 256); //nH
            //对于每一行，逐列打印
            for (int i = 0; i < bmp.getWidth(); i++) {
                //每一列24个像素点，分为3个字节存储
                for (int m = 0; m < 3; m++) {
                    //每个字节表示8个像素点，0表示白色，1表示黑色
                    for (int n = 0; n < 8; n++) {
                        byte b = px2Byte(i, j * 24 + m * 8 + n, bmp);
                        data[k] += data[k] + b;
                    }
                    k++;
                }
            }
            data[k++] = 10;//换行
        }
        return data;
    }

    private static byte px2Byte(int x, int y, Bitmap bit) {
        if (x < bit.getWidth() && y < bit.getHeight()) {
            byte b;
            int pixel = bit.getPixel(x, y);
            int red = (pixel & 0x00ff0000) >> 16; // 取高两位
            int green = (pixel & 0x0000ff00) >> 8; // 取中两位
            int blue = pixel & 0x000000ff; // 取低两位
            int gray = RGB2Gray(red, green, blue);
            if (gray < 128) {
                b = 1;
            } else {
                b = 0;
            }
            return b;
        }
        return 0;
    }

    private static int RGB2Gray(int r, int g, int b) {
        return (int) (0.29900 * r + 0.58700 * g + 0.11400 * b);
    }

    public interface Connection {
        void write(byte[] data);
    }

    private void printBitmap(Bitmap bmp, Connection connection) {
        if (null == bmp) {
            return;
        }

        byte[] data = new byte[]{0x1B, 0x33, 0x00};
        connection.write(data);
        data[0] = (byte) 0x00;
        data[1] = (byte) 0x00;
        data[2] = (byte) 0x00;    //重置参数

        int pixelColor;

        // ESC * m nL nH 点阵图
        byte[] escBmp = new byte[]{0x1B, 0x2A, 0x00, 0x00, 0x00};

        escBmp[2] = (byte) 0x21;

        //nL, nH
        escBmp[3] = (byte) (bmp.getWidth() % 256);
        escBmp[4] = (byte) (bmp.getWidth() / 256);

        // 每行进行打印
        for (int i = 0; i < bmp.getHeight() / 24 + 1; i++) {
            connection.write(escBmp);

            for (int j = 0; j < bmp.getWidth(); j++) {
                for (int k = 0; k < 24; k++) {
                    if (((i * 24) + k) < bmp.getHeight()) {
                        pixelColor = bmp.getPixel(j, (i * 24) + k);
                        if (pixelColor != -1) {
                            data[k / 8] += (byte) (128 >> (k % 8));
                        }
                    }
                }

                connection.write(data);
                // 重置参数
                data[0] = (byte) 0x00;
                data[1] = (byte) 0x00;
                data[2] = (byte) 0x00;
            }

            //换行
            byte[] byte_send1 = new byte[2];
            byte_send1[0] = 0x0d;
            byte_send1[1] = 0x0a;

            connection.write(byte_send1);
        }
    }

}
