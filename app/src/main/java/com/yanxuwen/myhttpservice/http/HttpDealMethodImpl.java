package com.yanxuwen.myhttpservice.http;

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
        //设置Cookie
        Map<String, String> headers = dealParams.getHeaders();
        headers.put("Cookie","JSESSIONID=AE7B1C9D73D448EEAECF5EC8363C55B0"
                + ";ClientVersion=6.8.1");
        dealParams.setHeaders(headers);
        //设置表单参数
        Map<String, String> mapField = dealParams.getMapField();
        mapField.put("reqcodeversion","6.8");
        //获取@Params里的参数，然后设置成json串，设置到表单body里
        Map<String, String> mapParams = dealParams.getParams();
        JSONObject jb = new JSONObject();
        String json = "";
        for (Map.Entry<String, String> entry : mapParams.entrySet()) {
            try {
                jb.put(entry.getKey(), entry.getValue());
            } catch (JSONException e) {
            }
        }
        json = jb.toString();
        mapField.put("body",json);

        dealParams.setMapField(mapField);
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
