package com.http.api;

import com.http.compiler.bean.MethodMeta;

import java.util.HashMap;
import java.util.Map;

public class DealParams {
    private int requestType = MethodMeta.TYPE.TYPE_GET;
    private String json;
    private Map<String, String> map;
    private Map<String, String> headers;
    private String url;
    private Map<String, String> params;

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

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

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public Map<String, String> getHeaders() {
        return headers == null ?  new HashMap<String, String>() : headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
