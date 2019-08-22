package com.http.api.bean;

import com.http.api.BaseDataCallBack;
import com.http.compiler.HttpDealMethod;
import com.http.compiler.bean.MethodMeta;

import java.util.Map;

/**
 * 自动生成用的请求参数
 */
public class RequestParams {
    private String url;
    @MethodMeta.TYPE
    private int requestType;
    private long timeout;
    private Map<String, String> mapField;
    private String json;
    private Map<String, Object> params;
    private Map<String, String> headers;
    private HttpDealMethod dealMethod;
    private BaseDataCallBack callback;
    private boolean syn;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public HttpDealMethod getDealMethod() {
        return dealMethod;
    }

    public void setDealMethod(HttpDealMethod dealMethod) {
        this.dealMethod = dealMethod;
    }

    public BaseDataCallBack getCallback() {
        return callback;
    }

    public void setCallback(BaseDataCallBack callback) {
        this.callback = callback;
    }

    public boolean isSyn() {
        return syn;
    }

    public void setSyn(boolean syn) {
        this.syn = syn;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public Map<String, String> getMapField() {
        return mapField;
    }

    public void setMapField(Map<String, String> mapField) {
        this.mapField = mapField;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
}
