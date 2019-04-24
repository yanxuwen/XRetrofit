package com.yanxuwen.myhttpservice;

import android.app.Application;

public class MyApplication extends Application  {
    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    public void init(){
//     不初始化的时候，会有默认值

//     初始化1、设置证书跟设置超时时间
//        OkHttpManger.getInstance()
//                .setSslSocketFactory(SslUtils.getSslSocketFactory()) //设置证书认证，从SslUtils类里面获取
//                .setTimeOut(10 * 1000,10 * 1000,10 * 1000);

//     初始化2、重置OkHttpClient
//       OkHttpManger.getInstance().setOkHttpClient(okHttpClient);
    }
}
