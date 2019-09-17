package com.chuangjiangx.print.impl.sunmisc.t2;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.os.RemoteException;

import com.chuangjiangx.print.PrintLogUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.IWoyouService;

/**
 * 商米T2等系列内置打印
 */
class AidlUtil {

    private static final String SERVICE＿PACKAGE = "woyou.aidlservice.jiuiv5";
    private static final String SERVICE＿ACTION = "woyou.aidlservice.jiuiv5.IWoyouService";

    private WeakReference<Context> mContext;
    private IWoyouService woyouService;

    private AidlUtil() {
    }

    private static class AidlUtilInner {
        private static AidlUtil instance = new AidlUtil();
    }

    static AidlUtil getInstance() {
        return AidlUtilInner.instance;
    }

    /**
     * 初始化打印机
     */
    void init(Context context) {
        this.mContext = new WeakReference<>(context);

        try {
            Intent intent = new Intent();
            intent.setPackage(SERVICE＿PACKAGE);
            intent.setAction(SERVICE＿ACTION);
            context.getApplicationContext().startService(intent);
            context.getApplicationContext().bindService(intent, connService, Context.BIND_AUTO_CREATE);

        } catch (Exception e) {
            PrintLogUtils.e(e, "初始化打印机失败！");
        }
    }

    /**
     * 关闭打印服务
     */
    void close() {
        if (null == woyouService) {
            return;
        }

        try {
            mContext.get().unbindService(connService);
            woyouService = null;

        } catch (Exception e) {
            PrintLogUtils.e(e, "关闭打印服务失败！");
        }
    }

    private ServiceConnection connService = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            woyouService = IWoyouService.Stub.asInterface(service);
            PrintLogUtils.d("打印服务绑定成功！");

            try {
                woyouService.printerInit(generateCB());

            } catch (RemoteException e) {
                PrintLogUtils.e(e, "打印服务初始化异常！");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            woyouService = null;
            PrintLogUtils.d("打印服务已关闭！");
        }
    };

    private ICallback generateCB() {
        return new ICallback.Stub() {
            @Override
            public void onRunResult(boolean isSuccess, int code, String msg) {
//                PrintLogUtils.d("ICallback：%b | %d | %s", isSuccess, code, msg);
            }
        };
    }

    boolean isConnect() {
        if (null == woyouService) {
            return false;
        }

        int printerState = 0;
        try {
            printerState = woyouService.updatePrinterState();

        } catch (Exception e) {
            PrintLogUtils.e(e, "");
        }

        return printerState == 1;
    }

    /**
     * 设置打印浓度
     */
    private int[] darkness = new int[]{0x0600, 0x0500, 0x0400, 0x0300, 0x0200, 0x0100, 0,
            0xffff, 0xfeff, 0xfdff, 0xfcff, 0xfbff, 0xfaff};

    void setDarkness(int index) {
        if (!isConnect()) {
            return;
        }

        int k = darkness[index];
        try {
            woyouService.sendRAWData(ESCUtil.setPrinterDarkness(k), null);
            woyouService.printerSelfChecking(null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 取得打印机系统信息，放在list中
     *
     * @return list
     */
    List<String> getPrinterInfo() {
        List<String> info = new ArrayList<>();

        if (!isConnect()) {
            return info;
        }

        try {
            info.add(woyouService.getPrinterSerialNo());
            info.add(woyouService.getPrinterModal());
            info.add(woyouService.getPrinterVersion());
            info.add(woyouService.getPrintedLength() + "");
            info.add("");
            //info.add(woyouService.getServiceVersion());
            PackageManager packageManager = mContext.get().getPackageManager();
            try {
                PackageInfo packageInfo = packageManager.getPackageInfo(SERVICE＿PACKAGE, 0);
                if (packageInfo != null) {
                    info.add(packageInfo.versionName);
                    info.add(packageInfo.versionCode + "");
                } else {
                    info.add("");
                    info.add("");
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return info;
    }

    /**
     * 打印条形码
     */
    void printBarCode(String data, int width, int height) {
        if (!isConnect()) {
            return;
        }

        try {
            woyouService.setAlignment(1, null);
            woyouService.printBarCode(data, 8, height, width, 0, null);
            woyouService.lineWrap(3, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印二维码
     */
    public void printQrCode(String qrCode, int width, int height) {
        if (!isConnect()) {
            return;
        }

        try {
            woyouService.setAlignment(1, null);
            woyouService.printQRCode(qrCode, 5, 3, null);
            woyouService.lineWrap(3, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印文字
     */
    void printText(String content, float size, boolean isBold, boolean isUnderLine) {
        if (!isConnect()) {
            return;
        }

        try {
            if (isBold) {
                woyouService.sendRAWData(ESCUtil.boldOn(), null);
            } else {
                woyouService.sendRAWData(ESCUtil.boldOff(), null);
            }

            if (isUnderLine) {
                woyouService.sendRAWData(ESCUtil.underlineWithOneDotWidthOn(), null);
            } else {
                woyouService.sendRAWData(ESCUtil.underlineOff(), null);
            }

            woyouService.printTextWithFont(content, null, size, null);
            woyouService.lineWrap(1, null);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印文字
     */
    void feed(int count) {
        if (!isConnect()) {
            return;
        }

        try {
            woyouService.lineWrap(count, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    void printTextCenter(String content, int size, boolean isBold, boolean isUnderLine) {
        try {
            woyouService.setAlignment(1, null);
            printText(content, size, isBold, isUnderLine);
            woyouService.setAlignment(0, null);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    void cut() {
        if (!isConnect()) {
            return;
        }

        try {
            woyouService.setAlignment(0, null);
            woyouService.cutPaper(null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印图片
     */
    void printBitmap(Bitmap bitmap) {
        if (!isConnect()) {
            return;
        }

        try {
            woyouService.setAlignment(1, null);
            woyouService.printBitmap(bitmap, null);
            woyouService.lineWrap(3, null);
            woyouService.setAlignment(0, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印图片和文字按照指定排列顺序
     */
    public void printBitmap(Bitmap bitmap, int orientation) {
        if (!isConnect()) {
            return;
        }

        try {
            if (orientation == 0) {
                woyouService.printBitmap(bitmap, null);
                woyouService.printText("横向排列\n", null);
                woyouService.printBitmap(bitmap, null);
                woyouService.printText("横向排列\n", null);

            } else {
                woyouService.printBitmap(bitmap, null);
                woyouService.printText("\n纵向排列\n", null);
                woyouService.printBitmap(bitmap, null);
                woyouService.printText("\n纵向排列\n", null);
            }
            woyouService.lineWrap(3, null);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 空打三行！
     */
    void print3Line() {
        if (!isConnect()) {
            return;
        }

        try {
            woyouService.lineWrap(3, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void sendRawData(byte[] data) {
        if (!isConnect()) {
            return;
        }

        try {
            woyouService.sendRAWData(data, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    // 获取当前的打印模式
    public int getPrintMode() {
        if (!isConnect()) {
            return -1;
        }

        int res;
        try {
            res = woyouService.getPrinterMode();
        } catch (RemoteException e) {
            e.printStackTrace();
            res = -1;
        }
        return res;
    }

    /**
     * 获取打印机当前状态
     */
    public String getPrinterState() {
        int printerState = 0;
        try {
            printerState = woyouService.updatePrinterState();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        switch (printerState) {
            case 1:
                return "打印机工作正常";
            case 2:
                return "打印机准备中";
            case 3:
                return "通讯异常";
            case 4:
                return "缺纸";
            case 5:
                return "过热";
            case 6:
                return "开盖";
            case 7:
                return "切刀异常";
            case 8:
                return "切刀恢复";
            case 9:
                return "未检测到⿊标";
            case 505:
                return "未检测到打印机";
            case 507:
                return "打印机固件升级失败";
            default:
                return "未知";
        }
    }

    /**
     * 是否还有打印纸
     */
    public boolean hasPaper() {
        int printerState = 0;
        try {
            printerState = woyouService.updatePrinterState();

        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return printerState != 4;
    }

}
