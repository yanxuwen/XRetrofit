package com.http.api;


import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.http.api.bean.RequestParams;
import com.http.compiler.HttpDealMethod;
import com.http.compiler.bean.DealParams;
import com.http.compiler.bean.MethodMeta;

import java.util.Map;

public class OkHttpUtils {

    public void request(String baseUrl, RequestParams requestParams) {
        if (!requestParams.getUrl().startsWith("http")) {
            requestParams.setUrl(baseUrl + requestParams.getUrl());
        }
        onDeal(requestParams);
    }

    /**
     * 上传使用
     */
    public void requestUpload(String baseUrl, RequestParams requestParams) {
        if (!requestParams.getUrl().startsWith("http")) {
            requestParams.setUrl(baseUrl + requestParams.getUrl());
        }
        onDealUpload(requestParams);
    }

    /**
     * 下载
     */
    public void requestDownload(String baseUrl, RequestParams requestParams) {
        if (!requestParams.getUrl().startsWith("http")) {
            requestParams.setUrl(baseUrl + requestParams.getUrl());
        }
        onDeal(requestParams);
    }

    private void onDeal(RequestParams requestParams) {
        if (requestParams.getDealMethod() != null) {
            RequestParams deal = (RequestParams) requestParams.getDealMethod().dealRequest((RequestParams) requestParams.clone());
            if (deal != null) {
                requestParams = deal;
            }
        }
        switch (requestParams.getRequestType()) {
            case MethodMeta.TYPE.TYPE_GET:
                OkHttpManger.getInstance().get(requestParams);
                break;
            case MethodMeta.TYPE.TYPE_POST:
            case MethodMeta.TYPE.TYPE_PUT:
            case MethodMeta.TYPE.TYPE_DELETE:
                OkHttpManger.getInstance().post(requestParams);
                break;
            case MethodMeta.TYPE.TYPE_DOWNLOAD:
                String filepath = "";
                String filename = "";
                //filepath
                Object o_filepath = requestParams.getParams().get("filepath");
                if (o_filepath instanceof String) {
                    filepath = (String) o_filepath;
                }
                //filename
                Object o_filename = requestParams.getParams().get("filename");
                if (o_filepath instanceof String) {
                    filename = (String) o_filename;
                }
                OkHttpManger.getInstance().downLoadFile(requestParams, filepath, filename);
                break;
            default:
                if (requestParams.getCallback() != null) {
                    requestParams.getCallback().postUIFail(new NetError(0, NetError.HttpErrorCode.DATA_ERROR, "参数错误", null), requestParams.isSyn());
                }
                break;
        }

    }

    private void onDealUpload(RequestParams requestParams) {
        if (requestParams.getDealMethod() != null) {
            RequestParams deal = (RequestParams) requestParams.getDealMethod().dealRequest((RequestParams) requestParams.clone());
            if (deal != null) {
                requestParams = deal;
            }
        }
        switch (requestParams.getRequestType()) {
            case MethodMeta.TYPE.TYPE_UPLOAD:
                String[] filepath = null;
                String[] filekey = null;
                String[] filename = null;
                if (requestParams.getParams() == null) {
                    requestParams.getCallback().postUIFail(new NetError(0, NetError.HttpErrorCode.DATA_ERROR, "参数错误", null), requestParams.isSyn());
                }
                //filepath
                Object o_filepath = requestParams.getParams().get("filepath");
                if (o_filepath != null && o_filepath instanceof String[]) {
                    filepath = (String[]) o_filepath;
                } else if (o_filepath != null && o_filepath instanceof Object[]) {
                    filepath = new String[((Object[]) o_filepath).length];
                    for (int i = 0; i < ((Object[]) o_filepath).length; i++) {
                        if (((Object[]) o_filepath)[i] instanceof String) {
                            filepath[i] = (String) ((Object[]) o_filepath)[i];
                        } else {
                            filepath[i] = "";
                        }
                    }
                }
                //filekey
                Object o_filekey = requestParams.getParams().get("filekey");
                if (o_filekey != null && o_filekey instanceof String[]) {
                    filekey = (String[]) o_filekey;
                } else if (o_filekey != null && o_filekey instanceof Object[]) {
                    filekey = new String[((Object[]) o_filekey).length];
                    for (int i = 0; i < ((Object[]) o_filekey).length; i++) {
                        if (((Object[]) o_filekey)[i] instanceof String) {
                            filekey[i] = (String) ((Object[]) o_filekey)[i];
                        } else {
                            filekey[i] = "";
                        }
                    }
                }
                //filename
                Object o_filename = requestParams.getParams().get("filename");
                if (o_filename != null && o_filename instanceof String[]) {
                    filename = (String[]) o_filename;
                } else if (o_filename != null && o_filename instanceof Object[]) {
                    filename = new String[((Object[]) o_filename).length];
                    for (int i = 0; i < ((Object[]) o_filename).length; i++) {
                        if (((Object[]) o_filename)[i] instanceof String) {
                            filename[i] = (String) ((Object[]) o_filename)[i];
                        } else {
                            filename[i] = "";
                        }
                    }
                }
                OkHttpManger.getInstance().upLoadFile(requestParams, filepath, filekey, filename);
                break;
            default:
                requestParams.getCallback().postUIFail(new NetError(0, NetError.HttpErrorCode.DATA_ERROR, "参数错误", null), requestParams.isSyn());
                break;
        }
    }
}