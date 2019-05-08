package com.yanxuwen.myhttpservice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.http.api.DealParams;
import com.http.api.HttpDealMethod;

import java.util.Map;

import okhttp3.OkHttpClient;

/**
 * 统一处理方法跟回调
 */
public class HttpDealMethodImpl implements HttpDealMethod {
    @Override
    public void init(OkHttpClient okHttpClient) {

    }

    @Override
    public DealParams dealRequest(DealParams dealParams) {
        Map<String, String> headers = dealParams.getHeaders();
        headers.put("Cookie","JSESSIONID=AE7B1C9D73D448EEAECF5EC8363C55B0"
                + ";ClientVersion=6.8.1");
        dealParams.setHeaders(headers);
        return dealParams;
    }

    @Override
    public String dealCallBack(String str) {
        String json = null;
        try {
            JSONObject jsonObject = JSON.parseObject(str);
            json = jsonObject.getString("body");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}
