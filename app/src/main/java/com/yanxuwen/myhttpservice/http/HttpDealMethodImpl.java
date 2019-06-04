package com.yanxuwen.myhttpservice.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.http.compiler.HttpDealMethod;
import com.http.compiler.bean.CallBack;
import com.http.compiler.bean.DealParams;

import java.util.Map;

/**
 * 统一处理方法跟回调
 */
public class HttpDealMethodImpl implements HttpDealMethod {

    /**
     * 处理请求
     * 如果处理后，各种字段都有，则会优先表单请求，然后再者json请求
     * 注意一定要return dealParams 不然不处理
     */
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

    /**
     * 处理回调,
     * 如果要设置返回错误，则new CallBack(-1,"请求失败") ，第一个参数不能为0即可，0代表成功
     * 如果要请求成功，直接 new CallBack(json)
     * return null 则不做任何处理
     */
    @Override
    public CallBack dealCallBack(String str) {
        String json = null;
        try {
            JSONObject jsonObject = JSON.parseObject(str);
            json = jsonObject.getString("body");
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        return  new CallBack(-1,"请求失败");
        return new CallBack(json);
    }
}
