package com.chuangjiangx.core.network.handler;


import androidx.annotation.NonNull;

import com.chuangjiangx.core.network.bean.CommonBean;
import com.chuangjiangx.core.network.error.HttpException;

import io.reactivex.functions.Function;

/**
 * 网络请求结果数据处理类
 *
 * @author: yangshuiqiang
 * Time:2017/11/22 14:42
 */

public class ModelHandler<T> implements Function<CommonBean, T> {
    @Override
    public T apply(@NonNull CommonBean commonBean) throws Exception {
        if (!commonBean.isSuccess() || commonBean.getData() == null) {//isSuccess未false时，抛出HttpException
            throw new HttpException(commonBean.getErrCode(), commonBean.getErrMsg());
        } else {
            //noinspection unchecked
            return (T) commonBean.getData();//isSuccess未true时，返回实体类
        }
    }
}
