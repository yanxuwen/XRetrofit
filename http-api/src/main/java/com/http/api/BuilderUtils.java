package com.http.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

public final class BuilderUtils {

    private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

    public OkHttpClient.Builder getBuilder() {
        return getBuilder(null);
    }

    public OkHttpClient.Builder getBuilder(Timeout timeout) {
        if (timeout == null){
            timeout = new Timeout();
        }
        OkHttpClient.Builder builder;
        builder = new OkHttpClient.Builder()
                .connectTimeout(timeout.getConnectTimeout(), TimeUnit.MILLISECONDS)
                .readTimeout(timeout.getReadTimeout(), TimeUnit.MILLISECONDS)
                .writeTimeout(timeout.getWriteTimeout(), TimeUnit.MILLISECONDS)
                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        cookieStore.put(url.host(), cookies);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        List<Cookie> cookies = cookieStore.get(url.host());
                        return cookies != null ? cookies : new ArrayList<Cookie>();
//自动管理Cookie发送Request都不用管Cookie这个参数也不用去response获取新Cookie什么的了。还能通过cookieStore获取当前保存的Cookie。
                    }
                });
        SslUtils.SSLParams sslParams = SslUtils.getSslSocketFactory();
        builder.sslSocketFactory(SslUtils.getSslSocketFactory().sSLSocketFactory, sslParams.trustManager);

        return builder;
    }
}
