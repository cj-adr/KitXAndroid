package com.chuangjiangx.print.impl.usb;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.text.TextUtils;

import com.chuangjiangx.print.PrintLogUtils;
import com.chuangjiangx.print.impl.BarUtils;
import com.chuangjiangx.print.impl.sunmisc.t2.ESCUtil;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * 外接USB打印(支持商米D2和TZH外接USB打印设备)
 */
class UsbPrinterUtils {

    private static final String ACTION_USB_PERMISSION = "com.usb.printer.USB_PERMISSION";
    private static final int TIME_OUT = 1000;

    private WeakReference<Context> mContext;
    private PendingIntent mPermissionIntent;
    private UsbManager mUsbManager;
    private UsbDevice mUsbDevice;
    private UsbInterface usbInterface;
    private UsbEndpoint printerEp;
    private UsbDeviceConnection mUsbDeviceConnection;
    private boolean isInit = false;

    private UsbPrinterUtils() {
    }

    private static class USBPrinterInner {
        private static UsbPrinterUtils mInstance = new UsbPrinterUtils();
    }

    static UsbPrinterUtils getInstance() {
        return USBPrinterInner.mInstance;
    }

    /**
     * 初始化打印机
     *
     * @param context 上下文
     */
    void init(Context context) {
        if (isInit) {
            return;
        }
        isInit = true;

        mContext = new WeakReference<>(context.getApplicationContext());

        registerReceiver();
        initUsbDevice(mContext.get());
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

            unregisterReceiver();

            if (null != mContext) {
                mContext.clear();
                mContext = null;
            }

            disconnect();

            mUsbManager = null;

        } catch (Exception e) {
            PrintLogUtils.e(e, "关闭打印机异常！");
        }
    }

    private void registerReceiver() {
        try {
            Context context = mContext.get();
            if (null == context) {
                return;
            }

            IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
            filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
            filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
            context.registerReceiver(mUsbDeviceReceiver, filter);

        } catch (Exception e) {
            PrintLogUtils.e(e, "注册打印机失败！");
        }
    }

    private void unregisterReceiver() {
        try {
            mContext.get().unregisterReceiver(mUsbDeviceReceiver);

        } catch (Exception e) {
            PrintLogUtils.e(e, "关闭打印机异常！");
        }
    }

    /**
     * 初始化USB设备
     */
    private void initUsbDevice(Context context) {
        mPermissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);
        mUsbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);

        if (null == mUsbManager) {
            return;
        }

        // 列出所有的USB设备，并且都请求获取USB权限
        HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();

        for (UsbDevice device : deviceList.values()) {
            if (handleUseDevice(device)) {
                break;
            }
        }
    }

    /**
     * 断开连接
     */
    private void disconnect() {
        try {
            mUsbDevice = null;
            usbInterface = null;
            printerEp = null;

            if (null != mUsbDeviceConnection) {
                mUsbDeviceConnection.close();
                mUsbDeviceConnection = null;
            }

        } catch (Exception e) {
            PrintLogUtils.e(e, "");
        }
    }

    private final BroadcastReceiver mUsbDeviceReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (null == context || null == intent) {
                return;
            }

            String action = intent.getAction();
            UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

            if (TextUtils.isEmpty(action) || null == usbDevice) {
                return;
            }

            if (ACTION_USB_PERMISSION.equals(action)) {
                if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                    if (isAvailable()) {
                        return;
                    }

                    connectUsbPrinter(usbDevice);
                }
                return;
            }

            if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                if (isAvailable()) {
                    if (mUsbDevice.getDeviceId() == usbDevice.getDeviceId()) {
                        PrintLogUtils.d("已拔出: " + usbDevice.getDeviceId() + "||" + usbDevice.getDeviceName());
                        disconnect();
                    }
                }
                return;
            }

            if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
                if (isAvailable()) {
                    // 当前有可用打印机，无需处理
                    return;
                }

                handleUseDevice(usbDevice);
            }
        }
    };

    private boolean handleUseDevice(UsbDevice device) {
        UsbInterface ui = device.getInterface(0);

        if (ui.getInterfaceClass() != UsbConstants.USB_CLASS_PRINTER) {
            return false;
        }

        usbInterface = ui;

        PrintLogUtils.d("已插入：" + device.getDeviceId() + "||" + device.getDeviceName());

        if (mUsbManager.hasPermission(device)) {
            connectUsbPrinter(device);

        } else {
            mUsbManager.requestPermission(device, mPermissionIntent);
        }

        return true;
    }

    /**
     * 连接打印机
     */
    private void connectUsbPrinter(UsbDevice usbDevice) {
        if (null == usbDevice || null == usbInterface) {
            return;
        }

        for (int i = 0; i < usbInterface.getEndpointCount(); i++) {
            UsbEndpoint ep = usbInterface.getEndpoint(i);
            if (ep.getType() != UsbConstants.USB_ENDPOINT_XFER_BULK) {
                continue;
            }

            if (ep.getDirection() == UsbConstants.USB_DIR_OUT) {
                mUsbDeviceConnection = mUsbManager.openDevice(usbDevice);

                if (null != mUsbDeviceConnection) {
                    printerEp = ep;
                    mUsbDevice = usbDevice;
                    mUsbDeviceConnection.claimInterface(usbInterface, true);
                    // 连接成功
                    PrintLogUtils.d("USB打印机连接成功");
                    break;
                }
            }
        }
    }

    /**
     * 打印机是否可用
     */
    boolean isAvailable() {
        return null != usbInterface && null != mUsbDevice && null != mUsbDeviceConnection && null != printerEp;
    }

    private void write(byte[] bytes) {
        if (!isAvailable()) {
            return;
        }

        mUsbDeviceConnection.bulkTransfer(printerEp, bytes, bytes.length, TIME_OUT);
    }

    /**
     * 打印文字
     */
    private void printText(String txt) {
        if (TextUtils.isEmpty(txt)) {
            return;
        }

        try {
            byte[] bytes = txt.getBytes("gbk");
            write(bytes);

        } catch (Exception e) {
            PrintLogUtils.e(e, "");
        }
    }

    /**
     * 换行打印文字
     */
    void printTextNewLine(String txt) {
        printText(txt);
        printLine(1);
    }

    /**
     * 打印空行
     */
    void printLine(int size) {
        for (int i = 0; i < size; i++) {
            printText("\n");
        }
    }

    /**
     * 设置字体大小
     *
     * @param size 0:正常大小 1:两倍高 2:两倍宽 3:两倍大小 4:三倍高 5:三倍宽 6:三倍大 7:四倍高 8:四倍宽 9:四倍大小 10:五倍高 11:五倍宽 12:五倍大小
     */
    void setTextSize(int size) {
        write(ESCUtil.setTextSize(size));
    }

    /**
     * 字体加粗
     */
    void bold(boolean isBold) {
        if (isBold) write(ESCUtil.boldOn());
        else write(ESCUtil.boldOff());
    }

    /**
     * 设置对齐方式
     */
    void setAlign(int position) {
        byte[] bytes = null;
        switch (position) {
            case 0:
                bytes = ESCUtil.alignLeft();
                break;
            case 1:
                bytes = ESCUtil.alignCenter();
                break;
            case 2:
                bytes = ESCUtil.alignRight();
                break;
        }
        write(bytes);
    }

    /**
     * 切纸
     */
    void cutPager() {
        write(ESCUtil.cutter());
    }

    /**
     * 打印一维条形码
     */
    void printBarCode(String data, int width, int height) {
        write(ESCUtil.getPrintBarCode(data, 5, height, width, 2));
    }

    /**
     * 打印二维码
     */
    public void printQrCode(String qrCode, int width, int height) {
        write(ESCUtil.getPrintQRCode(qrCode, 10, 3));
    }

    /**
     * 打印图片
     */
    void printBitmap(Bitmap bitmap) {
        setAlign(1);
        write(BarUtils.getBitmapPrintData(bitmap));
        setAlign(0);

        printLine(4);
    }

}