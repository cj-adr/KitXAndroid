package com.chuangjiangx.kitxandroid.network.network;


import com.chuangjiangx.core.network.bean.CommonBean;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author: yangshuiqiang
 * Time:2017/11/20 16:43
 */

public interface ApiServer {

    /**
     * 登录
     */
    @GET("base")
    Flowable<CommonBean<BaseBean>> getBase();


    /**
     * 登出
     */
    @GET("base-list")
    Flowable<CommonBean<BaseListBean>> getBaseList(@Query("pageNo") int pageNo);
}