package com.yanxuwen.http;

import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 *  统一处理方法跟回调
 */
public interface HttpDealMethod {
    public void init(OkHttpClient okHttpClient);
    /**
     * 处理请求
     */
    public DealParams dealRequest(DealParams dealParams);
    /**
     * 处理回调
     */
    public String dealCallBack(String str);
}
