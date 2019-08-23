package com.http.compiler.bean;

import com.http.compiler.HttpDealMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * 自动生成用的请求参数
 */
public class DealParams {
    private String url;
    @MethodMeta.TYPE
    private int requestType = MethodMeta.TYPE.TYPE_GET;
    private long timeout;
    private String json;
    private boolean syn;
    private int retry;
    private HttpDealMethod dealMethod;
    private Map<String, String> mapField;
    private Map<String, Object> params;
    private Map<String, String> headers;

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

    public HttpDealMethod getDealMethod() {
        return dealMethod;
    }

    public void setDealMethod(HttpDealMethod dealMethod) {
        this.dealMethod = dealMethod;
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

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public int getRetry() {
        return retry;
    }

    public void setRetry(int retry) {
        this.retry = retry;
    }


    public Map<String, Object> getParams() {
        return params = params == null ? new HashMap<>() : params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }


    public Map<String, String> getHeaders() {
        return headers = headers == null ? new HashMap<>() : headers;
    }


    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getMapField() {
        return mapField = mapField == null ? new HashMap<>() : mapField;
    }

    public void setMapField(Map<String, String> mapField) {
        this.mapField = mapField;
    }

    public void addParams(String key, String value) {
        getParams().put(key, value);
    }

    public void addHeader(String key, String value) {
        getHeaders().put(key, value);
    }

    public void addField(String key, String value) {
        getMapField().put(key, value);
    }
}
