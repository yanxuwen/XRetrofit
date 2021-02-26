package com.xretrofit.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * 自动生成用的请求参数
 */
public class RequestParams {
    private String url;
    private boolean isForm;//是否是表单提交
    private Map<String, Object> params;
    private Map<String, String> headers;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public Map<String, Object> getParams() {
        if (params == null){
            return new HashMap<>();
        }
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }


    public Map<String, String> getHeaders() {
        if (headers == null){
            return new HashMap<>();
        }
        return headers;
    }


    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void addParams(String key, String value) {
        getParams().put(key, value);
    }

    public void addHeader(String key, String value) {
        getHeaders().put(key, value);
    }

    public boolean isForm() {
        return isForm;
    }

    public void setForm(boolean form) {
        isForm = form;
    }
}
