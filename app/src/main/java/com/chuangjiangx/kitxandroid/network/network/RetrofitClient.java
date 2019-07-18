package com.chuangjiangx.kitxandroid.network.network;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * <pre>
 *     author : 杨水强
 *     time   : 2018/7/9
 *     desc   : 网络请求管理类
 *     version: 1.0
 * </pre>
 */

public class RetrofitClient {

    private volatile static RetrofitClient mRetrofitClient;

    private ApiServer mApiServer;

    private RetrofitClient() {
        //用Chrome 调试
        //请求头添加token
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)//设置连接超时时间
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addNetworkInterceptor(new StethoInterceptor())
                .build();

        mApiServer = new retrofit2.Retrofit.Builder()
                .baseUrl("http://47.110.224.157:3000/mock/36/")
                .addConverterFactory(GsonConverterFactory.create())//GSON转化器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//Rxjava转化器
                .client(okHttpClient)
                .build().create(ApiServer.class);

    }

    /**
     * 获取RetrofitClient对象
     *
     * @return RetrofitClient对象
     */
    private static RetrofitClient get() {
        if (mRetrofitClient == null) {
            synchronized (RetrofitClient.class) {
                if (mRetrofitClient == null) {
                    mRetrofitClient = new RetrofitClient();
                }
            }
        }
        return mRetrofitClient;
    }

    /**
     * Retrofit第一次初始化比较耗时，可以在Application中先初始化一次
     */
    public static void init() {
        RetrofitClient.get();
    }


    private ApiServer getApiserver() {
        return mApiServer;
    }


    public static ApiServer getServer() {
        return RetrofitClient.get().getApiserver();
    }
}