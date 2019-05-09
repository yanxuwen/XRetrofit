package com.http.api;

import com.http.compiler.bean.MethodMeta;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DealParams implements Serializable {
    private int requestType = MethodMeta.TYPE.TYPE_GET;
    private String url;
    private String json;
    private Map<String, String> mapField;
    private Map<String, String> headers;
    private Map<String, String> params;

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public Map<String, String> getMapField() {
        return mapField == null ?  new HashMap<String, String>() : mapField;
    }

    public void setMapField(Map<String, String> mapField) {
        this.mapField = mapField;
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
        return params == null ?  new HashMap<String, String>() : params;
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
    
    public Object deepClone() {
        ObjectInputStream oi = null;
        Object o = null;
        try {
            // 将对象写到流里
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(this);
            // 从流里读出来
            ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
            oi = new ObjectInputStream(bi);
            o = oi.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (o == null){
            o = new DealParams();
        }
        return o;
    }
}
