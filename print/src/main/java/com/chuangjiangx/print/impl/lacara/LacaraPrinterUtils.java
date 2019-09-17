package com.chuangjiangx.print.impl.lacara;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.os.IBinder;

import com.chuangjiangx.print.PrintLogUtils;
import com.chuangjiangx.print.impl.BarUtils;
import com.google.zxing.BarcodeFormat;
import com.lkl.cloudpos.aidl.AidlDeviceService;
import com.lkl.cloudpos.aidl.printer.AidlPrinter;
import com.lkl.cloudpos.aidl.printer.AidlPrinterListener;
import com.lkl.cloudpos.aidl.printer.PrintItemObj;
import com.lkl.cloudpos.data.PrinterConstant;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

final class LacaraPrinterUtils {

    private final static String ACTION_LKL = "lkl_cloudpos_mid_service";

    private WeakReference<Context> mContext;
    // 拉卡拉打印对象
    private AidlPrinter mPrinter;

    private LacaraPrinterUtils() {
    }

    private static class LacaraPrinterUtilsInner {
        private static LacaraPrinterUtils mInstance = new LacaraPrinterUtils();
    }

    static LacaraPrinterUtils getInstance() {
        return LacaraPrinterUtilsInner.mInstance;
    }

    void init(Context context) {
        mContext = new WeakReference<>(context.getApplicationContext());
        bindLkl();
    }

    private void bindLkl() {
        try {
            Intent intent = new Intent();
            intent.setAction(ACTION_LKL);
            mContext.get().bindService(getExplicitIntent(mContext.get(), intent), mServiceConnection, Context.BIND_AUTO_CREATE);

        } catch (Exception e) {
            PrintLogUtils.e(e, "bindLkl 拉卡拉打印服务失败\b");
        }
    }

    /**
     * 将意图转为显示意图
     *
     * @param implicitIntent 需要转换的意图
     * @return 转换后意图
     */
    private Intent getExplicitIntent(Context context, Intent implicitIntent) {
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);
        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);
        Intent explicitIntent = new Intent(implicitIntent);
        explicitIntent.setComponent(component);
        return explicitIntent;
    }

    private void unbindLkl() {
        try {
            mContext.get().unbindService(mServiceConnection);

        } catch (Exception e) {
            PrintLogUtils.e(e, "unbindLkl 拉卡拉打印服务失败\b");
        }
    }

    /**
     * 设别服务连接桥
     */
    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder serviceBinder) {
            if (serviceBinder != null) {
                //绑定成功
                AidlDeviceService serviceManager = AidlDeviceService.Stub.asInterface(serviceBinder);
                try {
                    mPrinter = AidlPrinter.Stub.asInterface(serviceManager.getPrinter());

                } catch (Exception e) {
                    PrintLogUtils.e(e, "bind 拉卡拉打印服务失败\b");
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            PrintLogUtils.d("拉卡拉打印服务已断开！");
            mPrinter = null;
        }
    };

    private AidlPrinterListener mListener = new AidlPrinterListener.Stub() {
        @Override
        public void onError(int i) {
//            if (i == 1) {
//                Toast.makeText(mContext.get(), "打印失败，请检查打印纸是否充足", Toast.LENGTH_SHORT).show();
//
//            } else if (i == 4) {
//                Toast.makeText(mContext.get(), "打印失败，请检查电量是否充足", Toast.LENGTH_SHORT).show();
//            }
        }

        @Override
        public void onPrintFinish() {
        }
    };

    boolean isAvailable() {
        try {
            return null != mPrinter &&
                    mPrinter.getPrinterState() == PrinterConstant.PrinterState.PRINTER_STATE_NORMAL;

        } catch (Exception e) {
            PrintLogUtils.e(e, "isAvailable 失败\b");
        }

        return false;
    }

    void close() {
        unbindLkl();
    }

    void printText(String text, boolean isCenter, boolean isLarge, boolean isBold) {
        try {
            // 拉卡拉打印条目
            List<PrintItemObj> printItemObjs = new ArrayList<>();
            printItemObjs.add(new PrintItemObj(text, isLarge ? 20 : 8, isBold, isCenter ? PrintItemObj.ALIGN.CENTER : PrintItemObj.ALIGN.LEFT));
            mPrinter.printText(printItemObjs, mListener);

        } catch (Exception e) {
            PrintLogUtils.e(e, "printText 失败\b");
        }
    }

    void printBarCode(String barCode, int width, int height) {
        try {
            mPrinter.printBarCode(width, height, 18, 73, barCode, mListener);

        } catch (Exception e) {
            PrintLogUtils.e(e, "printBarCode 失败\b");
        }
    }

    void printQrCode(String qrCode, int width, int height) {
        Bitmap bitmap = BarUtils.encodeAsBitmap(qrCode, BarcodeFormat.QR_CODE, width, height);
        printBitmap(bitmap);
    }

    void printBitmap(Bitmap bitmap) {
        try {
            mPrinter.printBmp(46, bitmap.getWidth(), bitmap.getHeight(), bitmap, mListener);

        } catch (Exception e) {
            PrintLogUtils.e(e, "printBitmap 失败\b");
        }
    }

    void feedPaper(int line) {
        for (int i = 0; i < line; i++) {
            printText("\n\n", false, false, false);
        }
    }

    void cutPaper() {
        feedPaper(2);
    }

}
