package com.chuangjiangx.core.network.bean;

import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *     author : 杨水强
 *     time   : 2018/7/9
 *     desc   : 网络请求基础数据类型
 *     version: 1.0
 * </pre>
 */
public class ResponseBean {

    private boolean success;
    @SerializedName(value = "errCode", alternate = {"err_code"})
    private String errCode;
    @SerializedName(value = "errMsg", alternate = {"err_msg"})
    private String errMsg;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrCode() {
        return this.errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return this.errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

}
