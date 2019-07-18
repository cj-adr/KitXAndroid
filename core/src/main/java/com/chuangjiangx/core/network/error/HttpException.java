package com.chuangjiangx.core.network.error;

/**
 * <pre>
 *     author : 杨水强
 *     time   : 2018/7/9
 *     desc   : 网络请求基础数据类型
 *     version: 1.0
 * </pre>
 */

public class HttpException extends Exception {

    public static final String NET_ERROR_CODE = "CJ_NET_BREAK";
    private String errCode;
    private String errMsg;


    public HttpException(String errCode, String errMsg) {
        super(errMsg);
        this.errCode = errCode;
        this.errMsg = errMsg;
        if (errCode == null) this.errCode = "";
        if (errMsg == null) this.errMsg = "";
        this.errMsg = this.errMsg.replaceAll("\\?", "");
    }


    /**
     * @return 是否为网络异常
     */
    public boolean isNetError() {
        return NET_ERROR_CODE.equals(errCode);
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
