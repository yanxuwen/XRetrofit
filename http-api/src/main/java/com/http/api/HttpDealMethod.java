package com.http.api;

import okhttp3.OkHttpClient;

/**
 *  统一处理方法跟回调
 */
public interface HttpDealMethod {
    public void init(OkHttpClient okHttpClient);
    /**
     * 处理请求
     * 如果处理后，各种字段都有，则会优先表单请求，然后再者json请求
     * 注意一定要return dealParams 不然不处理
     */
    public DealParams dealRequest(DealParams dealParams);
    /**
     * 处理回调
     */
    public String dealCallBack(String str);
}
