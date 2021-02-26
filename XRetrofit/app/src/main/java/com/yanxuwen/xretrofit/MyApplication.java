package com.yanxuwen.xretrofit;

import android.app.Application;

import com.xretrofit.HttpManager;
import com.xretrofit.okhttp.SslUtils;
import com.yanxuwen.xretrofit.converter.GsonConverterFactories;
import com.yanxuwen.xretrofit.converter.Rxjava2CallAdapterFactories;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    public void init() {

        OkHttpClient client = new OkHttpClient
                .Builder()
                .connectTimeout(15, TimeUnit.SECONDS)//连接超时时间
                .readTimeout(15, TimeUnit.SECONDS)//读取超时时间
                .writeTimeout(15, TimeUnit.SECONDS)//写入超时时间
                .retryOnConnectionFailure(false)//连接不上是否重连,false不重连
                .hostnameVerifier(new TrustAllHostnameVerifier())//校验名称,这个对象就是信任所有的主机,也就是信任所有https的请求
                .sslSocketFactory(SslUtils.getSslSocketFactory().sSLSocketFactory, SslUtils.getSslSocketFactory().trustManager)
                .build();
        HttpManager.Builder()
                .baseUrl("https://bxapi.bisinuolan.cn/")
                .addConverterFactory(GsonConverterFactories.create())//FastJson转换器、可以替换成Gson
                .addCallAdapterFactory(Rxjava2CallAdapterFactories.create())//Rxjava2适配器，不配置有自带Call适配器
                .client(client)
                .build();
    }

    /**
     * 信任所有的服务器,返回true
     */
    private static class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
}
