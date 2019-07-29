package com.chuangjiangx.print.impl.tzh;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import com.chuangjiangx.print.PrintLogUtils;
import com.chuangjiangx.print.Printable;
import com.printer.sdk.PrinterConstants;
import com.printer.sdk.PrinterInstance;
import com.printer.sdk.usb.USBPort;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

/***
 * 天之河
 */
public class TzhPrinter implements Printable {

    private static final String ACTION_USB_PERMISSION = "ACTION_USB_PERMISSION";

    private PrinterInstance mPrinterInstance;
    private boolean mIsConnect;

    private CountDownLatch mCountDownLatch = new CountDownLatch(1);

    @Override
    public int getType() {
        return PrintType.TZH;
    }

    @Override
    public void init(Context context) {
        new Thread(() -> {
            UsbManager manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
            if (null == manager) {
                return;
            }
            HashMap<String, UsbDevice> devices = manager.getDeviceList();
            if (null == devices || devices.isEmpty()) {
                return;
            }

            for (UsbDevice device : devices.values()) {
                if (!USBPort.isUsbPrinter(device)) {
                    continue;
                }

                mPrinterInstance = PrinterInstance.getPrinterInstance(context, device, null);
                mPrinterInstance.closeConnection();

                if (manager.hasPermission(device)) {
                    mIsConnect = mPrinterInstance.openConnection();
                    mCountDownLatch.countDown();

                } else {
                    try {
                        // 没有权限询问用户是否授予权限
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);
                        IntentFilter filter = new IntentFilter();
                        filter.addAction(ACTION_USB_PERMISSION);
                        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
                        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
                        filter.addAction(UsbManager.ACTION_USB_ACCESSORY_ATTACHED);
                        filter.addAction(UsbManager.ACTION_USB_ACCESSORY_DETACHED);
                        context.getApplicationContext().registerReceiver(mUsbReceiver, filter);
                        manager.requestPermission(device, pendingIntent); // 该代码执行后，系统弹出一个对话框

                    } catch (Exception e) {
                        PrintLogUtils.e(e, "");
                    }
                }
                break;
            }

        }).start();

        try {
            mCountDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void reconnect() {

    }

    @Override
    public void close() {
        if (mPrinterInstance != null && mIsConnect) {
            mPrinterInstance.closeConnection();
        }
    }

    @Override
    public boolean isAvailable() {
        return mPrinterInstance != null && mIsConnect;
    }

    @Override
    public boolean canReconnect() {
        return false;
    }

    @Override
    public void printText(String text, boolean center, boolean largeSize, boolean bold) {
        if (null == mPrinterInstance) {
            return;
        }

        if (center) {
            mPrinterInstance.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_CENTER);

        } else {
            mPrinterInstance.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_LEFT);
        }

        if (largeSize) {
            mPrinterInstance.setFont(0, 0, 1, bold ? 1 : 0, 0);

        } else {
            mPrinterInstance.setFont(0, 0, 0, bold ? 1 : 0, 0);
        }

        mPrinterInstance.printText(text + "\n");
    }

    @Override
    public void printBitmap(Bitmap bitmap) {
        if (null == mPrinterInstance) {
            return;
        }

        mPrinterInstance.printImage(bitmap, PrinterConstants.PAlign.CENTER, 0, false);
    }

    @Override
    public void printBarCode(String barCode) {
        if (null == mPrinterInstance) {
            return;
        }

        // TODO
    }

    @Override
    public void printQrCode(String qrCode) {
        if (null == mPrinterInstance) {
            return;
        }

        // TODO
    }

    @Override
    public void feedPaper(int line) {
        for (int i = 0; i < line; i++) {
            printText(" ", false, false, false);
        }
    }

    @Override
    public void cutPaper() {
        if (null == mPrinterInstance) {
            return;
        }

        mPrinterInstance.cutPaper(49, 0);
    }

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        @SuppressLint("NewApi")
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (!ACTION_USB_PERMISSION.equals(action)) {
                return;
            }

            UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
            if (!USBPort.isUsbPrinter(device)) {
                return;
            }

            if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                // 获取到权限
                try {
                    context.getApplicationContext().unregisterReceiver(mUsbReceiver);

                } catch (Exception e) {
                    PrintLogUtils.e(e, "");
                }

                mIsConnect = mPrinterInstance.openConnection();
            }

            mCountDownLatch.countDown();
        }
    };

}
