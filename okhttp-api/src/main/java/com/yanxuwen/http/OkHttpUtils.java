package com.yanxuwen.http;



import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.yanxuwen.compiler.bean.MethodMeta;

import java.util.Map;

import okhttp3.RequestBody;

public class OkHttpUtils {

    public void request(String baseUrl ,String url, int requestType, Map<String, String> map, String json, Map<String, String> params, Map<String, String> headers, HttpDealMethod httpDealMethod, final DataCallBack dataCallBack) {
        if (!url.startsWith("http")){
            url = baseUrl + url;
        }
        switch (requestType) {
            case MethodMeta.TYPE.TYPE_GET:
                get(url, headers, httpDealMethod, dataCallBack);
                break;
            case MethodMeta.TYPE.TYPE_POST:
                if (json != null) {
                    post(url, json, params, headers, httpDealMethod, dataCallBack);
                } else if (map != null) {
                    post(url, map, params, headers, httpDealMethod, dataCallBack);
                } else if (params != null) {
                    //先走json逻辑
                    post(url, "", params, headers, httpDealMethod, dataCallBack);
                }
                break;
        }
    }

    private void get(String url, Map<String, String> headers, HttpDealMethod httpDealMethod, final DataCallBack dataCallBack) {
        DealParams dealParams = new DealParams();
        dealParams.setRequestType(MethodMeta.TYPE.TYPE_GET);
        dealParams.setUrl(url);
        if (httpDealMethod != null) {
            dealParams = httpDealMethod.dealRequest(dealParams);
        }
        if (httpDealMethod != null && dealParams != null) {
            url = dealParams.getUrl();
            headers = dealParams.getHeaders();
        }
        OkHttpManger.getInstance().get(url, headers,httpDealMethod, dataCallBack);
    }

    private void post(String url, Map<String, String> map, Map<String, String> params, Map<String, String> headers, HttpDealMethod httpDealMethod, final DataCallBack dataCallBack) {
        DealParams dealParams = new DealParams();
        dealParams.setRequestType(MethodMeta.TYPE.TYPE_POST);
        dealParams.setUrl(url);
        dealParams.setMap(map);
        dealParams.setParams(params);
        if (httpDealMethod != null) {
            dealParams = httpDealMethod.dealRequest(dealParams);
        }
        if (httpDealMethod != null && dealParams != null) {
            url = dealParams.getUrl();
            map = dealParams.getMap();
            headers = dealParams.getHeaders();
            params = dealParams.getParams();
        }
        OkHttpManger.getInstance().post(url, map, headers,httpDealMethod, dataCallBack);
    }

    private void post(String url, String json, Map<String, String> params, Map<String, String> headers, HttpDealMethod httpDealMethod, final DataCallBack dataCallBack) {
        DealParams dealParams = new DealParams();
        dealParams.setRequestType(MethodMeta.TYPE.TYPE_POST);
        dealParams.setUrl(url);
        dealParams.setJson(json);
        dealParams.setParams(params);
        if (httpDealMethod != null) {
            dealParams = httpDealMethod.dealRequest(dealParams);
        }
        if (httpDealMethod != null && dealParams != null) {
            url = dealParams.getUrl();
            json = dealParams.getJson();
            headers = dealParams.getHeaders();
            params = dealParams.getParams();
        }
        //json为空，params不为空，当json来使用
        if ((json == null || json.equals("")) && params != null) {
            JSONObject jb = new JSONObject();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                try {
                    jb.put(entry.getKey(), entry.getValue());
                } catch (JSONException e) {
                }
            }
            json = jb.toString();
        }
        OkHttpManger.getInstance().post(url, json, headers,httpDealMethod, dataCallBack);
    }

    public void post(String url, RequestBody requestBody, Map<String, String> headers,  HttpDealMethod httpDealMethod,final DataCallBack dataCallBack) {
        OkHttpManger.getInstance().post(url, requestBody, headers, httpDealMethod,dataCallBack);
    }
}
