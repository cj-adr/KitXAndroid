package com.chuangjiangx.core.network.handler;


import androidx.annotation.NonNull;

import com.chuangjiangx.core.network.bean.Null;
import com.chuangjiangx.core.network.bean.ResponseBean;
import com.chuangjiangx.core.network.error.HttpException;

import io.reactivex.functions.Function;

/**
 * 网络请求结果数据处理类
 *
 * @author: yangshuiqiang
 * Time:2017/11/22 14:42
 */

public class ResponseModelHandler implements Function<ResponseBean, Null> {
    @Override
    public Null apply(@NonNull ResponseBean responseBean) throws Exception {
        if (!responseBean.isSuccess()) {//isSuccess未false时，抛出HttpException异常
            throw new HttpException(responseBean.getErrCode(), responseBean.getErrMsg());
        } else {
            return new Null();
        }
    }
}
