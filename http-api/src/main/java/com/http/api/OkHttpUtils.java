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
        onDeal(requestType, url, json, mapField, headers, params, httpDealMethod, dataCallBack, null, syn);
    }

    /**
     * 上传使用
     */
    public void requestUpload(String baseUrl, String url, int requestType, Map<String, String> mapField, String json, Map<String, Object[]> params, Map<String, String> headers, HttpDealMethod httpDealMethod, final ProgressCallBack dataCallBack, boolean syn) {
        if (!url.startsWith("http")) {
            url = baseUrl + url;
        }
        onDealUpload(requestType, url, json, mapField, headers, params, httpDealMethod, dataCallBack, dataCallBack, syn);
    }

    /**
     * 下载
     */
    public void requestDownload(String baseUrl, String url, int requestType, Map<String, String> mapField, String json, Map<String, String> params, Map<String, String> headers, HttpDealMethod httpDealMethod, final ProgressCallBack progressCallBack, boolean syn) {
        if (!url.startsWith("http")) {
            url = baseUrl + url;
        }
        onDeal(requestType, url, json, mapField, headers, params, httpDealMethod, null, progressCallBack, syn);
    }

    private void onDeal(int requestType, String url, String json, Map<String, String> mapField, Map<String, String> headers, Map<String, String> params, final HttpDealMethod httpDealMethod, final DataCallBack dataCallBack, final ProgressCallBack progressCallBack, boolean syn) {
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
                OkHttpManger.getInstance().get(dealParams.getUrl(), requestType, dealParams.getHeaders(), httpDealMethod, dataCallBack, syn);
                break;
            case MethodMeta.TYPE.TYPE_POST:
            case MethodMeta.TYPE.TYPE_PUT:
            case MethodMeta.TYPE.TYPE_DELETE:
                if (dealParams.getMapField() != null && !dealParams.getMapField().isEmpty()) {
                    //表单提交
                    OkHttpManger.getInstance().post(dealParams.getUrl(), requestType, dealParams.getMapField(), dealParams.getHeaders(), httpDealMethod, dataCallBack, syn);

                } else if (dealParams.getJson() != null && !dealParams.getJson().equals("")) {
                    //json提交
                    OkHttpManger.getInstance().post(dealParams.getUrl(), requestType, dealParams.getJson(), dealParams.getHeaders(), httpDealMethod, dataCallBack, syn);

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
                    OkHttpManger.getInstance().post(dealParams.getUrl(), requestType, json, dealParams.getHeaders(), httpDealMethod, dataCallBack, syn);

                } else {
                    OkHttpManger.getInstance().post(dealParams.getUrl(), requestType, "", dealParams.getHeaders(), httpDealMethod, dataCallBack, syn);
                }
                break;
            case MethodMeta.TYPE.TYPE_DOWNLOAD:
                String filepath = dealParams.getParams().get("filepath");
                String filename = dealParams.getParams().get("filename");
                OkHttpManger.getInstance().downLoadFile(dealParams.getUrl(), filepath, filename, dealParams.getHeaders(), httpDealMethod, progressCallBack, syn);
                break;
            default:
                dataCallBack.postUIFail(new NetError(0, NetError.HttpErrorCode.DATA_ERROR, "参数错误", null), syn);
                break;
        }

    }

    private void onDealUpload(int requestType, String url, String json, Map<String, String> mapField, Map<String, String> headers, Map<String, Object[]> params, final HttpDealMethod httpDealMethod, final ProgressCallBack dataCallBack, final ProgressCallBack progressCallBack, boolean syn) {
        DealParams dealParams = new DealParams();
        dealParams.setRequestType(requestType);
        dealParams.setUrl(url);
        dealParams.setJson(json);
        dealParams.setMapField(mapField);
        dealParams.setHeaders(headers);
        if (httpDealMethod != null) {
            DealParams deal = httpDealMethod.dealRequest((DealParams) dealParams.deepClone());
            if (deal != null) {
                dealParams = deal;
            }
        }
        switch (requestType) {
            case MethodMeta.TYPE.TYPE_UPLOAD:
                String[] filepath = null;
                String[] filekey = null;
                String[] filename = null;
                if (params == null){
                    dataCallBack.postUIFail(new NetError(0, NetError.HttpErrorCode.DATA_ERROR, "参数错误", null), syn);
                }
                //filepath
                Object[] o_filepath = params.get("filepath");
                if (o_filepath!= null && o_filepath instanceof String[]){
                    filepath = (String[]) o_filepath;
                } else if (o_filepath != null && o_filepath instanceof Object[]){
                    filepath = new String[o_filepath.length];
                     for (int i = 0 ; i < o_filepath.length  ; i++){
                         if (o_filepath[i] instanceof String){
                             filepath[i] = (String) o_filepath[i];
                         } else {
                             filepath[i] = "";
                         }
                     }
                }
                //filekey
                Object[] o_filekey = params.get("filekey");
                if (o_filekey!= null && o_filekey instanceof String[]){
                    filekey = (String[]) o_filekey;
                } else if (o_filekey != null && o_filekey instanceof Object[]){
                    filekey = new String[o_filekey.length];
                    for (int i = 0 ; i < o_filekey.length  ; i++){
                        if (o_filekey[i] instanceof String){
                            filekey[i] = (String) o_filekey[i];
                        } else {
                            filekey[i] = "";
                        }
                    }
                }
                //filename
                Object[] o_filename = params.get("filename");
                if (o_filename!= null && o_filename instanceof String[]){
                    filename = (String[]) o_filename;
                } else if (o_filename != null && o_filename instanceof Object[]){
                    filename = new String[o_filename.length];
                    for (int i = 0 ; i < o_filename.length  ; i++){
                        if (o_filename[i] instanceof String){
                            filename[i] = (String) o_filename[i];
                        } else {
                            filename[i] = "";
                        }
                    }
                }
                OkHttpManger.getInstance().upLoadFile(dealParams.getUrl(), filepath,  filekey, filename, dealParams.getMapField(), dealParams.getHeaders() , httpDealMethod, progressCallBack, syn);
                break;
            default:
                dataCallBack.postUIFail(new NetError(0, NetError.HttpErrorCode.DATA_ERROR, "参数错误", null), syn);
                break;
        }
    }
}