package com.chuangjiangx.print.escpos;

import android.graphics.Bitmap;

/**
 * 支持ESC/POS热敏打印机
 */
public final class ESCPOSUtil {

    private static final byte ESC = 0x1B;
    private static final byte GS = 0x1D;

    private ESCPOSUtil() {
    }

    /**
     * 初始化打印机，清除打印缓存区数据
     */
    public static byte[] init() {
        return new byte[]{ESC, 0x40};
    }

    /**
     * 切纸
     */
    public static byte[] cut() {
        return new byte[]{GS, 0x56, 0x41, 0};
    }

    /**
     * 设置字符右间距
     *
     * @param padding 0~255(点)
     */
    public static byte[] setFontPadding(int padding) {
        return new byte[]{ESC, 0x20, (byte) padding};
    }

    /**
     * 设置字体大小
     *
     * @param widthScale  1~8,表示放大倍数
     * @param heightScale 1~8,表示放大倍数
     */
    public static byte[] setFontSize(int widthScale, int heightScale) {
        byte mode = Byte.valueOf((widthScale - 1) + "00" + (heightScale - 1), 16);

        return new byte[]{GS, 0x21, mode};
    }

    /**
     * 设置字体加粗
     *
     * @param bold 是否加粗
     */
    public static byte[] setFontBold(boolean bold) {
        if (bold) {
            return new byte[]{ESC, 0x45, 1};
        }

        return new byte[]{ESC, 0x45, 0};
    }

    /**
     * 设置默认行高 30点
     */
    public static byte[] setDefaultLineSpace() {
        return new byte[]{ESC, 0x32};
    }

    /**
     * 设置行高
     *
     * @param space 0~255
     */
    public static byte[] setLineSpace(int space) {
        return new byte[]{ESC, 0x33, (byte) space};
    }

    /**
     * 设置下划线
     *
     * @param mode 0:取消下划线 1:选择下划线（1点宽） 2:选择下划线（2点宽）
     */
    public static byte[] setUnderline(int mode) {
        return new byte[]{ESC, 0x2D, (byte) mode};
    }

    /**
     * 设置字符对齐模式
     *
     * @param mode 0:左对齐 1:居中 2:右对齐
     */
    public static byte[] setAlign(int mode) {
        return new byte[]{ESC, 0x61, (byte) mode};
    }

    /**
     * 走纸
     *
     * @param line 字符行数
     */
    public static byte[] printWrapLine(int line) {
        return new byte[]{ESC, 0x64, (byte) line};
    }

    /**
     * 打印条码（默认打印CODE128）
     */
    public static byte[] printBarCode(String code, int width, int height) {
        try {
            if (width < 2 || width > 6) {
                width = 2;
            }

            if (height < 1 || height > 255) {
                height = 162;
            }

            byte[] markCmd = new byte[]{GS, 0x48, 0}; // 注释字符位置，默认不打印
            byte[] widthCmd = new byte[]{GS, 0x77, (byte) width};
            byte[] heightCmd = new byte[]{GS, 0x68, (byte) height};
            byte[] dataCmd = code.getBytes("GB18030");
            byte[] codeCmd = new byte[]{GS, 0x6B, 0x49, (byte) dataCmd.length}; // CODE128

            return byteMerger(init(), markCmd, widthCmd, heightCmd, codeCmd, dataCmd);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return printWrapLine(1);
    }

    /**
     * 打印二维码
     *
     * @param moduleSize 1~16 默认4
     * @param errorLevel 0~3 默认0
     */
    public static byte[] printQrCode(String code, int moduleSize, int errorLevel) {
        try {
            // 设置二维码块大小
            byte[] sizeCmd = new byte[]{GS, 0x28, 0x6B, 0x03, 0x00, 0x31, 0x43, (byte) moduleSize};

            // 设置容错级别
            byte[] errorLevelCmd = new byte[]{GS, 0x28, 0x6B, 0x03, 0x00, 0x31, 0x45, (byte) (48 + errorLevel)};

            // 设置二维码数据
            byte[] dataCmd = code.getBytes("GB18030");
            int len = dataCmd.length + 3;
            byte[] dataPreCmd = new byte[]{GS, 0x28, 0x6B, 0x03, (byte) len, (byte) (len >> 8), 0x51, 0x30};

            // 打印
            byte[] printCmd = new byte[]{GS, 0x28, 0x6B, 0x03, 0x00, 0x31, 0x51, 0x30};

            return byteMerger(sizeCmd, errorLevelCmd, dataPreCmd, dataCmd, printCmd);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return printWrapLine(1);
    }

    /**
     * 使用光栅位图的打印方式
     */
    public static byte[] printBitmap(Bitmap bitmap, int requestWidth) {
        // GS v 0 m xL xH yL yH d1...dk
        // 规范化位图宽高
        bitmap = resizeImage(bitmap, requestWidth);

        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

        int width = bitmapWidth / 8;

        byte[] cmd = new byte[width * bitmapHeight + 4 + 4];
        cmd[0] = 29;
        cmd[1] = 118;
        cmd[2] = 48;
        cmd[3] = 0;
        cmd[4] = (byte) (width % 256);//计算xL
        cmd[5] = (byte) (width / 256);//计算xH
        cmd[6] = (byte) (bitmapHeight % 256);//计算yL
        cmd[7] = (byte) (bitmapHeight / 256);//计算yH

        int index = 8;
        int temp;
        int[] part = new int[8];

        for (int j = 0; j < bitmapHeight; j++) {
            for (int i = 0; i < bitmapWidth; i += 8) {
                // 横向每8个像素点组成一个字节。
                for (int k = 0; k < 8; k++) {
                    int pixel = bitmap.getPixel(i + k, j);
                    int grayPixel = grayPixel(pixel);
                    if (grayPixel > 128) {
                        //灰度值大于128位   白色 为第k位0不打印
                        part[k] = 0;

                    } else {
                        part[k] = 1;
                    }
                }

                // 128千万不要写成2^7，^是异或操作符
                temp = part[0] * 128 +
                        part[1] * 64 +
                        part[2] * 32 +
                        part[3] * 16 +
                        part[4] * 8 +
                        part[5] * 4 +
                        part[6] * 2 +
                        part[7];

                cmd[index++] = (byte) temp;
            }
        }

        return cmd;
    }

    private static Bitmap resizeImage(Bitmap bitmap, int requestWidth) {
        //将位图宽度规范化为8的整数倍
        int legalWidth = (requestWidth + 7) / 8 * 8;
        int height = (int) (legalWidth * bitmap.getHeight() / (float) bitmap.getWidth());
        return Bitmap.createScaledBitmap(bitmap, legalWidth, height, true);
    }

    private static int grayPixel(int pixel) {
        int red = (pixel & 0x00ff0000) >> 16;//获取r分量
        int green = (pixel & 0x0000ff00) >> 8;//获取g分量
        int blue = pixel & 0x000000ff;//获取b分量
        return (int) (red * 0.3f + green * 0.59f + blue * 0.11f);//加权平均法进行灰度化
    }

    private static byte[] byteMerger(byte[]... byteArr) {
        if (null == byteArr || byteArr.length <= 0) {
            return new byte[0];
        }

        int size = 0;
        for (byte[] arr : byteArr) {
            size += arr.length;
        }
        byte[] cmd = new byte[size];

        int index = 0;
        for (byte[] arr : byteArr) {
            System.arraycopy(arr, 0, cmd, index, arr.length);
            index += arr.length;
        }

        return cmd;
    }

}
