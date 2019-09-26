package com.chuangjiangx.print.impl.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;

import com.chuangjiangx.print.PrintLogUtils;
import com.chuangjiangx.print.impl.BaseEscPrintUtils;

import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.UUID;

/**
 * 蓝牙打印
 */
final class BluetoothPrinterUtils extends BaseEscPrintUtils {

    // 蓝牙打印所需的UUID
    private static final UUID BLUETOOTH_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

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

    @Override
    protected void write(byte[] bytes) {
        try {
            mOutputStream.write(bytes);
            mOutputStream.flush();

        } catch (Exception ignored) {
        }
    }

}
