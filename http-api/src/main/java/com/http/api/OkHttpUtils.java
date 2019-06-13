package com.http.api;


import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.http.compiler.HttpDealMethod;
import com.http.compiler.bean.DealParams;
import com.http.compiler.bean.MethodMeta;

import java.util.Map;

public class OkHttpUtils {

    public void request(String baseUrl, String url, int requestType, Map<String, String> mapField, String json, Map<String, String> params, Map<String, String> headers, HttpDealMethod httpDealMethod, final DataCallBack dataCallBack, boolean syn) {
        if (!url.startsWith("http")) {
            url = baseUrl + url;
        }
        onDeal(requestType , url ,json ,mapField ,headers ,params,httpDealMethod,dataCallBack,syn);
    }

    private void onDeal(int requestType, String url, String json, Map<String, String> mapField, Map<String, String> headers, Map<String, String> params, HttpDealMethod httpDealMethod, final DataCallBack dataCallBack, boolean syn) {
        DealParams dealParams = new DealParams();
        dealParams.setRequestType(requestType);
        dealParams.setUrl(url);
        dealParams.setJson(json);
        dealParams.setMapField(mapField);
        dealParams.setHeaders(headers);
        dealParams.setParams(params);
        if (httpDealMethod != null) {
            DealParams deal = httpDealMethod.dealRequest((DealParams) dealParams.deepClone());
            if (deal != null) {
                dealParams = deal;
            }
        }
        switch (requestType) {
            case MethodMeta.TYPE.TYPE_GET:
                OkHttpManger.getInstance().get(dealParams.getUrl(), dealParams.getHeaders(), httpDealMethod, dataCallBack, syn);
                break;
            case MethodMeta.TYPE.TYPE_POST:
                if (dealParams.getMapField() != null && !dealParams.getMapField().isEmpty()) {
                    //表单提交
                    OkHttpManger.getInstance().post(dealParams.getUrl(), dealParams.getMapField(), dealParams.getHeaders(), httpDealMethod, dataCallBack, syn);

                } else if (dealParams.getJson() != null && !dealParams.getJson().equals("")) {
                    //json提交
                    OkHttpManger.getInstance().post(dealParams.getUrl(), dealParams.getJson(), dealParams.getHeaders(), httpDealMethod, dataCallBack, syn);

                } else if (dealParams.getParams() != null && !dealParams.getParams().isEmpty()) {
                    //json提交
                    JSONObject jb = new JSONObject();
                    for (Map.Entry<String, String> entry : params.entrySet()) {
                        try {
                            jb.put(entry.getKey(), entry.getValue());
                        } catch (JSONException e) {
                        }
                    }
                    json = jb.toString();
                    OkHttpManger.getInstance().post(dealParams.getUrl(), json, dealParams.getHeaders(), httpDealMethod, dataCallBack, syn);

                } else {
                    dataCallBack.postUIFail(new NetError(0,NetError.HttpErrorCode.DATA_ERROR, "参数错误", null), syn);
                }
                break;
            default:
                dataCallBack.postUIFail(new NetError(0,NetError.HttpErrorCode.DATA_ERROR, "参数错误", null), syn);
                break;
        }

    }
}