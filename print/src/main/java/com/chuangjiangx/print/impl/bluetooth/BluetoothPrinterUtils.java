package com.chuangjiangx.print.impl.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.graphics.Bitmap;

import com.chuangjiangx.print.PrintLogUtils;

import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.UUID;

/**
 * 蓝牙打印
 */
class BluetoothPrinterUtils {

    // 蓝牙打印所需的UUID
    private static final UUID BLUETOOTH_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private static final byte[] COMMAND_CLEAR_FORMAT = {0x1b, 0x40}; //复位打印机
    private static final byte[] COMMAND_CENTER = {0x1b, 0x61, 0x01};//居中指令
    private static final byte[] COMMAND_DOUBLE_HEIGHT = {0x1d, 0x21, 0x01}; //高加倍
    private static final byte[] COMMAND_WIDTH = {0x1D, 0x77, 0x02};//设置条码宽
    private static final byte[] COMMAND_HEIGHT = {0x1D, 0x68, 0x50};//设置条码高
    private static final byte[] COMAND_TOP_FROMAT = {0x1d, 0x48, 0x00}; //设置条码样式
    private static final byte[] COMMAND_ONE_CODE = {0x1D, 0x6B, 0x49, 0x0E, 0x7B, 0x43};//一维码指令

    private WeakReference<Context> mContext;
    // 蓝牙SOCKET对象
    private BluetoothSocket mBluetoothSocket;
    // 蓝牙SOCKET输出流
    private OutputStream mOutputStream;
    private boolean isInit = false;
    private BluetoothPrinter.BluetoothConnectListener mListener;

    private BluetoothPrinterUtils() {
    }

    private static class BluetoothPrinterUtilsInner {
        static BluetoothPrinterUtils instance = new BluetoothPrinterUtils();
    }

    public static BluetoothPrinterUtils getInstance() {
        return BluetoothPrinterUtilsInner.instance;
    }

    /**
     * 初始化打印机
     */
    void init(Context context, String address, BluetoothPrinter.BluetoothConnectListener listener) {
        if (isInit) {
            return;
        }
        isInit = true;

        this.mContext = new WeakReference<>(context.getApplicationContext());
        this.mListener = listener;

        new Thread(() -> connectDevice(address)).start();
    }

    /**
     * 关闭打印机
     */
    void close() {
        try {
            if (!isInit) {
                return;
            }
            isInit = false;

            if (null != mContext) {
                mContext.clear();
                mContext = null;
            }

            disconnect();

        } catch (Exception e) {
            PrintLogUtils.e(e, "");
        }
    }

    /**
     * 连接蓝牙设备
     */
    void connectDevice(String address) {
        try {
            disconnect();

            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);

            mBluetoothSocket = device.createRfcommSocketToServiceRecord(BLUETOOTH_UUID);
            mBluetoothSocket.connect();
            mOutputStream = mBluetoothSocket.getOutputStream();

            PrintLogUtils.d("蓝牙打印机连接成功！");

            if (null != mListener) {
                mListener.onConnectSuccess(address);
            }

        } catch (Exception e) {
            PrintLogUtils.e(e, "");
            disconnect();

            if (null != mListener) {
                mListener.onConnectFail(address, e);
            }
        }
    }

    /**
     * 蓝牙设备断开
     */
    private void disconnect() {
        try {
            if (mOutputStream != null) {
                mOutputStream.close();
            }

        } catch (Exception e) {
            PrintLogUtils.e(e, "");

        } finally {
            mOutputStream = null;
        }

        try {
            if (mBluetoothSocket != null && mBluetoothSocket.isConnected()) {
                mBluetoothSocket.close();
            }

        } catch (Exception e) {
            PrintLogUtils.e(e, "");

        } finally {
            mBluetoothSocket = null;
        }
    }

    /**
     * 是否可用
     */
    boolean isAvailable() {
        if (null == mBluetoothSocket || null == mOutputStream || !mBluetoothSocket.isConnected()) {
            return false;
        }

        return testWrite();
    }

    private boolean testWrite() {
        boolean isAvailable = true;

        try {
            mOutputStream.write(new byte[]{0});
            mOutputStream.flush();

        } catch (Exception ignored) {
            isAvailable = false;
        }

        return isAvailable;
    }

    /**
     * 打印文字
     */
    void printText(String text, boolean isCenter, boolean isLarge, boolean isBold) {
        try {
            byte[] textBytes = (text + "\r\n").getBytes("gbk");
            mOutputStream.write(COMMAND_CLEAR_FORMAT);
            if (isCenter) {
                mOutputStream.write(COMMAND_CENTER);
            }
            if (isLarge) {
                mOutputStream.write(COMMAND_DOUBLE_HEIGHT);
            }
            mOutputStream.write(textBytes);
            mOutputStream.flush();

        } catch (Exception e) {
            PrintLogUtils.e(e, "");
        }
    }

    /**
     * 打印图片
     */
    void printBitmap(Bitmap bitmap) {
        // TODO bitmap打印
    }

    /**
     * 打印机走纸，通过调用打印两行空白文字实现
     */
    void feedPaper(int count) {
        for (int i = 0; i < count; i++) {
            printText("\n", false, false, false);
        }
    }

    /**
     * 切纸
     */
    void cutPaper() {
        feedPaper(1);
    }

    /**
     * 打印条形码
     */
    void printBarCode(String text) {
        try {
            byte[] b = new byte[13];
            int i = 0;
            while (2 * i < text.length()) {
                String s = text.substring(2 * i, 2 * i + 2);
                b[i] = Byte.parseByte(s);
                i++;
            }
            //一维码打印
            mOutputStream.write(COMMAND_CLEAR_FORMAT);
            mOutputStream.write(COMMAND_HEIGHT);
            mOutputStream.write(COMMAND_WIDTH);
            mOutputStream.write(COMMAND_CENTER);
            mOutputStream.write(COMAND_TOP_FROMAT);
            mOutputStream.write(COMMAND_ONE_CODE);
            mOutputStream.write(b);
            mOutputStream.flush();

        } catch (Exception e) {
            PrintLogUtils.e(e, "");
        }
    }

    /**
     * 打印二维码
     */
    void printQrCode(String text) {
        try {
            Integer pl = (text.length() + 3) % 256;
            Integer ph = (text.length() + 3) / 256;
            mOutputStream.write(COMMAND_CLEAR_FORMAT);
            mOutputStream.write(new byte[]{0x1B, 0x61, 0x01});
            mOutputStream.write(new byte[]{0x1D, 0x28, 0x6B, 0x03, 0x00, 0x31, 0x43, 0x08});
            mOutputStream.write(new byte[]{0x1D, 0x28, 0x6B, 0x03, 0x00, 0x31, 0x45, 0x30});
            mOutputStream.write(new byte[]{0x1D, 0x28, 0x6B, pl.byteValue(), ph.byteValue(), 0x31, 0x50, 0x30});
            mOutputStream.write(text.getBytes());
            mOutputStream.write(new byte[]{0x1D, 0x28, 0x6B, 0x03, 0x00, 0x31, 0x51, 0x30});
            mOutputStream.flush();

        } catch (Exception e) {
            PrintLogUtils.e(e, "");
        }
    }

}
