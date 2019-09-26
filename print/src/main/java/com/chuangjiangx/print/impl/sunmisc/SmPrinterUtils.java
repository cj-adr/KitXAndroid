package com.chuangjiangx.print.impl.sunmisc;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.os.RemoteException;

import com.chuangjiangx.print.PrintLogUtils;
import com.chuangjiangx.print.escpos.ESCPOSUtil;
import com.chuangjiangx.print.impl.BaseEscPrintUtils;

import java.lang.ref.WeakReference;

import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.IWoyouService;

/**
 * 商米T2等系列内置打印
 */
final class SmPrinterUtils extends BaseEscPrintUtils {

    private static final String SERVICE_PACKAGE = "woyou.aidlservice.jiuiv5";
    private static final String SERVICE_ACTION = "woyou.aidlservice.jiuiv5.IWoyouService";

    private WeakReference<Context> mContext;
    private IWoyouService woyouService;

    private SmPrinterUtils() {
    }

    private static class SmPrinterUtilsInner {
        private static SmPrinterUtils instance = new SmPrinterUtils();
    }

    static SmPrinterUtils getInstance() {
        return SmPrinterUtilsInner.instance;
    }

    /**
     * 初始化打印机
     */
    void init(Context context) {
        this.mContext = new WeakReference<>(context);

        try {
            Intent intent = new Intent();
            intent.setPackage(SERVICE_PACKAGE);
            intent.setAction(SERVICE_ACTION);
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

    /**
     * 获取打印机当前状态
     */
    private String getPrinterState() {
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

    private boolean isConnect() {
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
     * 是否还有打印纸
     */
    private boolean hasPaper() {
        int printerState = 0;
        try {
            printerState = woyouService.updatePrinterState();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return printerState != 4;
    }

    boolean isAvailable() {
        return isConnect() && hasPaper();
    }

    @Override
    protected void write(byte[] bytes) {
        try {
            woyouService.sendRAWData(bytes, null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initPrinter() {
        try {
            woyouService.setAlignment(0, null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cutPaper() {
        try {
            printWrapLine(4);
            woyouService.cutPaper(null);
            initPrinter();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void printText(String content, boolean isCenter, boolean isLargeSize, boolean isBold, boolean isUnderLine) {
        try {
            if (isCenter) {
                woyouService.setAlignment(1, null);

            } else {
                woyouService.setAlignment(0, null);
            }

            woyouService.sendRAWData(ESCPOSUtil.setFontBold(isBold), null);
            woyouService.sendRAWData(ESCPOSUtil.setUnderline(isUnderLine ? 1 : 0), null);

            woyouService.printTextWithFont(content, null, isLargeSize ? 40 : 28, null);

            printWrapLine(1);

            woyouService.setAlignment(0, null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void printBitmap(Bitmap bitmap, int position) {
        try {
            woyouService.setAlignment(position, null);
            woyouService.printBitmap(bitmap, null);

            printWrapLine(1);
            woyouService.setAlignment(0, null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void printWrapLine(int count) {
        try {
            woyouService.lineWrap(count, null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void printBarCode(String data, int width, int height) {
        try {
            woyouService.setAlignment(1, null);
            woyouService.printBarCode(data, 8, height, width, 0, null);

            printWrapLine(1);
            woyouService.setAlignment(0, null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void printQrCode(String qrCode, int moduleSize) {
        try {
            woyouService.setAlignment(1, null);
            woyouService.printQRCode(qrCode, moduleSize, 3, null);

            printWrapLine(1);
            woyouService.setAlignment(0, null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
