package com.xretrofit;

import com.xretrofit.CallAdapter.CallAdapter;
import com.xretrofit.CallAdapter.ObjectCallAdapterFactory;
import com.xretrofit.converter.Converter;
import com.xretrofit.converter.DownloadConverterFactories;
import com.xretrofit.converter.StringConverterFactories;
import com.xretrofit.okhttp.SslUtils;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

import static com.xretrofit.utils.Utils.checkNotNull;

/**
 * @author bsnl_yanxuwen
 * @date 2021/2/4 14:00
 * Description :
 * 配置
 */
public final class HttpManager {
    private static volatile HttpManager instance;
    public List<Converter.Factory> converterFactories = new ArrayList<>();
    public List<CallAdapter.Factory> callAdapterFactories = new ArrayList<>();
    public String baseUrl;
    okhttp3.Call.Factory callFactory;

    public static HttpManager getInstance() {
        if (instance == null) {
            synchronized (HttpManager.class) {
                if (instance == null) {
                    instance = new HttpManager();
                }
            }
        }

        return instance;
    }

    HttpManager() {

    }

    public okhttp3.Call.Factory callFactory() {
        if (callFactory == null) {
            return callFactory = getOkHttpClient();
        }
        return callFactory;

    }

    /**
     * 文件下载用的，不能跟其他共享，避免其他错误。
     */
    public OkHttpClient getOkHttpDownloadClient(Interceptor interceptor) {
        OkHttpClient client = new OkHttpClient
                .Builder()
                .connectTimeout(15, TimeUnit.SECONDS)//连接超时时间
                .readTimeout(15, TimeUnit.SECONDS)//读取超时时间
                .writeTimeout(15, TimeUnit.SECONDS)//写入超时时间
                .retryOnConnectionFailure(false)//连接不上是否重连,false不重连
                .sslSocketFactory(SslUtils.getSslSocketFactory().sSLSocketFactory, SslUtils.getSslSocketFactory().trustManager)
                .addNetworkInterceptor(interceptor)
                .build();
        return client;
    }

    /**
     * 默认OkHttpClient
     */
    public OkHttpClient getOkHttpClient() {
        OkHttpClient client = new OkHttpClient
                .Builder()
                .connectTimeout(15, TimeUnit.SECONDS)//连接超时时间
                .readTimeout(15, TimeUnit.SECONDS)//读取超时时间
                .writeTimeout(15, TimeUnit.SECONDS)//写入超时时间
                .retryOnConnectionFailure(false)//连接不上是否重连,false不重连
                .sslSocketFactory(SslUtils.getSslSocketFactory().sSLSocketFactory, SslUtils.getSslSocketFactory().trustManager)
                .build();
        return client;
    }

    void setData(@Nullable Call.Factory callFactory, String baseUrl,
                 List<Converter.Factory> converterFactories,
                 List<CallAdapter.Factory> callAdapterFactories) {

        //默认添加一个字符串转换器、下载转换器
        converterFactories.add(0, DownloadConverterFactories.create());
        converterFactories.add(StringConverterFactories.create());

        //添加适配器
        callAdapterFactories.add(ObjectCallAdapterFactory.create());

        this.baseUrl = baseUrl;
        this.converterFactories = converterFactories;
        this.callAdapterFactories = callAdapterFactories;
        this.callFactory = callFactory;

    }

    public static Builder Builder() {
        return new Builder();
    }

    public static final class Builder {
        private List<Converter.Factory> converterFactories = new ArrayList<>();
        private List<CallAdapter.Factory> callAdapterFactories = new ArrayList<>();
        private String baseUrl;
        private @Nullable
        okhttp3.Call.Factory callFactory;

        public Builder() {
        }

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = checkNotNull(baseUrl, "baseUrl == null");
            return this;
        }

        public Builder addConverterFactory(Converter.Factory factory) {
            converterFactories.add(checkNotNull(factory, "factory == null"));
            return this;
        }

        public Builder addCallAdapterFactory(CallAdapter.Factory factory) {
            callAdapterFactories.add(checkNotNull(factory, "factory == null"));
            return this;
        }

        public Builder client(OkHttpClient client) {
            return callFactory(checkNotNull(client, "client == null"));
        }

        public Builder callFactory(okhttp3.Call.Factory factory) {
            this.callFactory = checkNotNull(factory, "factory == null");
            return this;
        }

        public void build() {
            HttpManager.getInstance().setData(callFactory, baseUrl, converterFactories, callAdapterFactories);
        }
    }


    public static String packageName = "com.http";

    /**
     * 照抄ElementUtils
     */
    public static String getImplName(Class<?> clazz) {
        return packageName + "." + clazz.getSimpleName() + "$Impl";
    }
}
