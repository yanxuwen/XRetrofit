package com.http.api;


import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.http.api.Interceptor.DownloadResponseBody;
import com.http.api.bean.RequestParams;
import com.http.compiler.bean.CallBack;
import com.http.compiler.bean.MethodMeta;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.net.ConnectException;
import java.net.FileNameMap;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.EventListener;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class OkHttpManger {
    private OkHttpClient okHttpClient;
    private OkHttpClient.Builder builder;
    private volatile static OkHttpManger instance;
    private Timeout timeout;
    private HashMap<Call, BaseDataCallBack> callBackHashMap;//正常请求

    //提交json数据
    private static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
    //提交字符串数据
    private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown;charset=utf-8");

    // 使用getCacheDir()来作为缓存文件的存放路径（/data/data/包名/cache） ，
// 如果你想看到缓存文件可以临时使用 getExternalCacheDir()（/sdcard/Android/data/包名/cache）。
    private static File cacheFile;
    private static Cache cache;

    public OkHttpManger() {
        callBackHashMap = new HashMap<>();
    }


    public static OkHttpManger getInstance() {
        if (instance == null) {
            synchronized (OkHttpManger.class) {
                if (instance == null) {
                    instance = new OkHttpManger();
                }
            }
        }
        return instance;
    }

    /**
     * 设置认证，根据需求从SslUtils类里面取
     */
    public OkHttpManger setSslSocketFactory(SslUtils.SSLParams sslParams) {
        if (sslParams != null && getBuilder() != null) {
            getBuilder().sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        }
        return this;
    }

    /**
     * 所有的请求，设置超时时间，单位毫秒，
     */
    public OkHttpManger setTimeOut(long connectTimeout, long readTimeout, long writeTimeout) {
        timeout = new Timeout(connectTimeout, readTimeout, writeTimeout);
        return this;
    }

    public void setOkHttpClient(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    public OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            getBuilder().eventListener(new EventListener() {
                @Override
                public void callStart(Call call) {
                    if (callBackHashMap.containsKey(call)) {
                        callBackHashMap.get(call).postUIStart(call);
                    }
                }
            });
            okHttpClient = getBuilder().build();
        }
        return okHttpClient;
    }

    /**
     * 文件下载用的，不能跟其他共享，避免其他错误。
     */
    private OkHttpClient getOkHttpDownloadClient(Interceptor interceptor) {
        OkHttpClient okHttpClient;
        OkHttpClient.Builder builder = new BuilderUtils().getBuilder(timeout);
        builder.addNetworkInterceptor(interceptor);
        builder.eventListener(new EventListener() {
            @Override
            public void callStart(Call call) {
                if (callBackHashMap.containsKey(call)) {
                    callBackHashMap.get(call).postUIStart(call);
                }
            }
        });
        okHttpClient = builder.build();
        return okHttpClient;
    }

    private OkHttpClient.Builder getBuilder() {
        if (builder == null) {
            builder = new BuilderUtils().getBuilder(timeout);
        }
        return builder;
    }

    /**
     * get同步请求不需要传参数
     * 通过response.body().string()获取返回的字符串
     *
     * @param url
     * @return
     */
    public String getString(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = getOkHttpClient().newCall(request);
        try {
            Response response = call.execute();
            // 将response转化成String
            String responseStr = response.body().string();
            return responseStr;
        } catch (IOException e) {

        }
        return null;
    }

    /**
     * get同步请求
     * 通过response.body().bytes()获取返回的二进制字节数组
     *
     * @param url
     * @return
     */
    public byte[] getByte(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = getOkHttpClient().newCall(request);
        try {
            Response response = call.execute();
            // 将response转化成String
            byte[] responseStr = response.body().bytes();
            return responseStr;
        } catch (IOException e) {

        }
        return null;
    }

    /**
     * get同步请求
     * 通过response.body().byteStream()获取返回的二进制字节流
     *
     * @param url
     * @return
     */
    public InputStream getInputStream(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = getOkHttpClient().newCall(request);
        try {
            Response response = call.execute();
            // 将response转化成String
            InputStream responseStr = response.body().byteStream();
            return responseStr;
        } catch (IOException e) {

        }
        return null;
    }

    /**
     * get同步请求
     * 通过response.body().byteStream()获取返回的二进制字节流
     *
     * @param url
     * @return
     */
    public Reader getReader(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = getOkHttpClient().newCall(request);
        try {
            Response response = call.execute();
            // 将response转化成String
            Reader responseStr = response.body().charStream();
            return responseStr;
        } catch (IOException e) {

        }
        return null;
    }

    /**
     * get异步请求
     *
     * @return
     */
    public void get(final RequestParams requestParams) {
        final Request request = buildRequest(requestParams.getUrl(), requestParams.getRequestType(), null, requestParams.getHeaders());
        Call call = getOkHttpClient().newCall(request);
        if (requestParams.getTimeout() > 0) {
            call.timeout().timeout(requestParams.getTimeout(), TimeUnit.MILLISECONDS);
        }
        callBackHashMap.put(call, requestParams.getCallback());
        try {
            // 请求加入调度
            call.enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    callBackHashMap.remove(call);
                    postUIFail(e, requestParams);
                }

                @Override
                public void onResponse(Call call, Response response) {
                    callBackHashMap.remove(call);
                    postUISuccess(response, requestParams);
                }
            });
        } catch (Exception e) {

        }
    }


    /**
     * post异步请求
     *
     * @return
     */
    public void post(final RequestParams requestParams) {
        String realURL = UrlUtils.urlJoint(requestParams.getUrl(), null);
        RequestBody requestBody;
        Request request;
        if (requestParams.getMapField() != null && !requestParams.getMapField().isEmpty()) {
            //表单提交
            FormBody.Builder builder = new FormBody.Builder();
            /**
             * 在这对添加的参数进行遍历
             */
            addMapParmsToFromBody(requestParams.getMapField(), builder);
            requestBody = builder.build();
            request = buildRequest(realURL, requestParams.getRequestType(), requestBody, requestParams.getHeaders());
        } else if (requestParams.getJson() != null && !requestParams.getJson().equals("")) {
            //json提交
            request = buildJsonPostRequest(realURL, requestParams.getRequestType(), requestParams.getJson(), requestParams.getHeaders());
        } else if (requestParams.getParams() != null && !requestParams.getParams().isEmpty()) {
            //json提交
            JSONObject jb = new JSONObject();
            for (Map.Entry<String, Object> entry : requestParams.getParams().entrySet()) {
                try {
                    jb.put(entry.getKey(), entry.getValue());
                } catch (JSONException e) {
                }
            }
            String json = jb.toString();
            request = buildJsonPostRequest(realURL, requestParams.getRequestType(), json, requestParams.getHeaders());
        } else {
            request = buildJsonPostRequest(realURL, requestParams.getRequestType(), "", requestParams.getHeaders());
        }


        Call call = getOkHttpClient().newCall(request);
        if (requestParams.getTimeout() > 0) {
            call.timeout().timeout(requestParams.getTimeout(), TimeUnit.MILLISECONDS);
        }
        callBackHashMap.put(call, requestParams.getCallback());
        try {
            // 请求加入调度
            call.enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    callBackHashMap.remove(call);
                    postUIFail(e, requestParams);
                }

                @Override
                public void onResponse(Call call, Response response) {
                    callBackHashMap.remove(call);
                    postUISuccess(response, requestParams);
                }
            });
        } catch (Exception e) {

        }
    }

    /**
     * 基于http的文件上传（传入文件数组和key）混合参数和文件请求
     * 通过addFormDataPart可以添加多个上传的文件
     *
     * @param filePath  上传的文件
     * @param fileKeys  上传的文件key集合
     * @param fileNames 文件名字，如果没有值，则取路径名字
     */
    public void upLoadFile(final RequestParams requestParams, final String[] filePath, String[] fileKeys, String[] fileNames) {
        if (filePath == null || filePath.length == 0) {
            requestParams.getCallback().postUIFail(requestParams, new NetError(0, NetError.HttpErrorCode.DATA_EMPTY, "上传文件不能为空", null), requestParams.isSyn());
            return;
        }
        if (fileKeys == null || fileKeys.length == 0) {
            requestParams.getCallback().postUIFail(requestParams, new NetError(0, NetError.HttpErrorCode.DATA_EMPTY, "上传文件key不能为空", null), requestParams.isSyn());
            return;
        }
        if (filePath.length != fileKeys.length) {
            requestParams.getCallback().postUIFail(requestParams, new NetError(0, NetError.HttpErrorCode.DATA_EMPTY, "文件路径跟文件key个数不相等", null), requestParams.isSyn());
            return;
        }
        if (requestParams.getMapField() == null) {
            requestParams.setMapField(new HashMap<String, String>());
        }
        final String realURL = UrlUtils.urlJoint(requestParams.getUrl(), null);
        FormBody.Builder builder = new FormBody.Builder();
        addMapParmsToFromBody(requestParams.getMapField(), builder);
        RequestBody requestBody = builder.build();
        MultipartBody.Builder multipartBody = new MultipartBody.Builder();
        multipartBody.setType(MultipartBody.FORM)
                .addPart(requestBody);

        final DecimalFormat df = new DecimalFormat("#.00");
        final float[] totalProgress = new float[filePath.length];
        final File[] files = new File[filePath.length];
        for (int i = 0; i < filePath.length; i++) {
            files[i] = new File(filePath[i]);
        }
        if (files != null) {
            RequestBody fileBody = null;
            for (int i = 0; i < files.length; i++) {
                final int finalI = i;
                File file = files[finalI];
                String fileName;
                if (fileNames != null && fileNames.length > i) {
                    fileName = fileNames[i];
                } else {
                    fileName = file.getName();
                }
                fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
                UploadResponseBody filePart = new UploadResponseBody(fileBody, new ProgressCallBack() {

                    @Override
                    public void onLoadProgress(float progress) {
                        totalProgress[finalI] = progress;
                        double process = 0;
                        for (double mProcess : totalProgress) {
                            process += mProcess;
                        }
                        if (requestParams.getCallback() instanceof ProgressCallBack) {
                            ((ProgressCallBack) requestParams.getCallback()).postUILoading(Float.valueOf(df.format(process / files.length)), requestParams.isSyn());
                        }
                    }

                    @Override
                    public void onHttpSuccess(Object path) {

                    }

                    @Override
                    public void onHttpFail(NetError netError) {

                    }
                });
                //TODO 根据文件名设置contentType
                multipartBody.addPart(Headers.of("Content-Disposition",
                        "form-data; name=\"" + fileKeys[i] + "\"; filename=\"" + fileName + "\""),
                        filePart);
            }

        }
        final Request request = buildRequest(realURL, MethodMeta.TYPE.TYPE_POST, multipartBody.build(), requestParams.getHeaders());
        Call call = getOkHttpClient().newCall(request);
        if (requestParams.getTimeout() > 0) {
            call.timeout().timeout(requestParams.getTimeout(), TimeUnit.MILLISECONDS);
        }
        callBackHashMap.put(call, requestParams.getCallback());
        // 请求加入调度
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                callBackHashMap.remove(call);
                postUIFail(e, requestParams);
            }

            @Override
            public void onResponse(Call call, Response response) {
                callBackHashMap.remove(call);
                String json;
                try {
                    json = response.body().string();
                    postLoadSuccess(call, response, json, requestParams);
                } catch (Exception e) {
                    requestParams.getCallback().postUIFail(requestParams, new NetError(response.code(), NetError.HttpErrorCode.DATA_ERROR, "数据错误", e.getMessage()), requestParams.isSyn());
                    return;
                }
            }
        });
    }

    /**
     * 文件下载
     *
     * @param destFileDir 本地存储的文件夹路径
     * @param fileName    文件名字
     */
    public void downLoadFile(final RequestParams requestParams, final String destFileDir, String fileName) {
        final String realURL = UrlUtils.urlJoint(requestParams.getUrl(), null);
        if (destFileDir == null || destFileDir.equals("")) {
            requestParams.getCallback().postUIFail(requestParams, new NetError(0, NetError.HttpErrorCode.FILE_NOT_FOUND, "文件路径不存在", null), requestParams.isSyn());
            return;
        }
        File folder = new File(destFileDir);
        if (!folder.exists()) {
            try {
                boolean isMkdirs = folder.mkdirs();
                if (!isMkdirs) {
                    throw new FileNotFoundException("java.io.FileNotFoundException: " + folder + ": open failed: ENOENT (No such file or directory)");
                }
            } catch (Exception e) {
                requestParams.getCallback().postUIFail(requestParams, new NetError(0, NetError.HttpErrorCode.FILE_NOT_FOUND, "文件创建失败", e.getMessage()), requestParams.isSyn());
                return;
            }
        }
        final File file = new File(folder.getPath(), (fileName == null || fileName.equals("")) ? getFileName(requestParams.getUrl()) : fileName);
        final long startsPoint = file.length() > 0 ? file.length() - 1 : file.length();
        requestParams.addHeader("RANGE", "bytes=" + startsPoint + "-");//断点续传
        Request request = buildRequest(realURL, MethodMeta.TYPE.TYPE_DOWNLOAD, null, requestParams.getHeaders());
        // 重写ResponseBody监听请求
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder()
                        .body(new DownloadResponseBody(originalResponse, startsPoint, requestParams.getCallback(), requestParams.isSyn()))
                        .build();
            }
        };
        Call call = getOkHttpDownloadClient(interceptor).newCall(request);
        if (requestParams.getTimeout() > 0) {
            call.timeout().timeout(requestParams.getTimeout(), TimeUnit.MILLISECONDS);
        }
        callBackHashMap.put(call, requestParams.getCallback());
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBackHashMap.remove(call);
                postUIFail(e, requestParams);
            }

            @Override
            public void onResponse(Call call, Response response) {
                callBackHashMap.remove(call);
                ResponseBody responseBody = response.body();
                long length = responseBody.contentLength();
                if (length == 0 || length == 1) {
                    // 说明文件已经下载完，直接跳转安装就好
                    postLoadSuccess(call, response, String.valueOf(file.getAbsoluteFile()), requestParams);
                    return;
                }
                if (responseBody instanceof DownloadResponseBody) {
                    ((DownloadResponseBody) responseBody).setMaxProgress(length + startsPoint);
                }
                // 保存文件到本地
                InputStream is = null;
                RandomAccessFile randomAccessFile = null;
                BufferedInputStream bis = null;

                byte[] buff = new byte[2048];
                int len = 0;
                try {
                    is = responseBody.byteStream();
                    bis = new BufferedInputStream(is);

                    // 随机访问文件，可以指定断点续传的起始位置
                    randomAccessFile = new RandomAccessFile(file, "rwd");
                    randomAccessFile.seek(startsPoint);
                    while ((len = bis.read(buff)) != -1) {
                        randomAccessFile.write(buff, 0, len);
                    }

                    // 下载完成
                    postLoadSuccess(call, response, String.valueOf(file.getAbsoluteFile()), requestParams);
                } catch (Exception e) {
                    postUIFail(e, requestParams);
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (bis != null) {
                            bis.close();
                        }
                        if (randomAccessFile != null) {
                            randomAccessFile.close();
                        }
                    } catch (Exception e) {

                    }
                }

            }
        });
    }

    private void addMapParmsToFromBody(Map<String, String> params, FormBody.Builder builder) {
        for (Map.Entry<String, String> map : params.entrySet()) {
            String key = map.getKey();
            String value;
            /**
             * 判断值是否是空的
             */
            if (map.getValue() == null) {
                value = "";
            } else {
                value = map.getValue();
            }
            /**
             * 把key和value添加到formbody中
             */
            builder.add(key, value);
        }
    }

    private Request buildRequest(String url, @MethodMeta.TYPE int requestType, RequestBody body, Map<String, String> headers) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        if (body != null) {
            switch (requestType) {
                case MethodMeta.TYPE.TYPE_POST:
                    builder.post(body);
                    break;
                case MethodMeta.TYPE.TYPE_PUT:
                    builder.put(body);
                    break;
                case MethodMeta.TYPE.TYPE_DELETE:
                    builder.delete(body);
                    break;
            }
        } else if (requestType == MethodMeta.TYPE.TYPE_GET) {
            builder.get();
        }
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        return builder.build();
    }

    /**
     * String_POST请求参数
     *
     * @param url  url
     * @param json json
     * @return requestBody
     */
    private Request buildStringPostRequest(String url, int requestType, String json, Map<String, String> headers) {
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_MARKDOWN, json);
        return buildRequest(url, requestType, requestBody, headers);
    }

    /**
     * Json_POST请求参数
     *
     * @param url  url
     * @param json json
     * @return requestBody
     */
    private Request buildJsonPostRequest(String url, int requestType, String json, Map<String, String> headers) {
//        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        RequestBody requestBody = RequestBody.create(JSON, json);
        return buildRequest(url, requestType, requestBody, headers);
    }

    private String getFileName(String url) {
        int separatorIndex = url.lastIndexOf("/");
        return (separatorIndex < 0) ? url : url.substring(separatorIndex + 1, url.length());

    }

    private String guessMimeType(String fileName) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(fileName);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    private void postUIFail(Exception e, RequestParams requestParams) {
        if (requestParams.getCallback() != null) {
            if (e instanceof SocketTimeoutException || (e instanceof InterruptedIOException && e.getMessage().equals("timeout"))) {
                //InterruptedIOException 该判断只限制4.1.0判断，如果版本有变更，则换别的判断
                //超时
                requestParams.getCallback().postUIFail(requestParams, new NetError(0, NetError.HttpErrorCode.NET_TIMEOUT, "请求超时", e.getMessage()), requestParams.isSyn());
            } else if (e instanceof ConnectException || e instanceof UnknownHostException) {
                requestParams.getCallback().postUIFail(requestParams, new NetError(0, NetError.HttpErrorCode.NET_DISCONNECT, "网络异常", e.getMessage()), requestParams.isSyn());
            } else if (e instanceof SocketException) {
                requestParams.getCallback().postUIFail(requestParams, new NetError(0, NetError.HttpErrorCode.NET_DISCONNECT, "Socket closed", e.getMessage()), requestParams.isSyn());
            } else if (e instanceof FileNotFoundException) {
                requestParams.getCallback().postUIFail(requestParams, new NetError(0, NetError.HttpErrorCode.ERROR, "文件找不到", e.getMessage()), requestParams.isSyn());
            } else if (e instanceof java.io.IOException && e.getMessage().equals("Canceled")) {
                //该判断只限制4.1.0判断，如果版本有变更，则换别的判断
                requestParams.getCallback().postUIFail(requestParams, new NetError(0, NetError.HttpErrorCode.CANCEL, "取消", e.getMessage()), requestParams.isSyn());
            } else {
                requestParams.getCallback().postUIFail(requestParams, new NetError(0, NetError.HttpErrorCode.ERROR, "错误", e.getMessage()), requestParams.isSyn());
            }
        }
    }

    private void postUISuccess(Response response, RequestParams requestParams) {
        String json;
        try {
            json = response.body().string();
        } catch (Exception e) {
            if (requestParams.getCallback() != null) {
                requestParams.getCallback().postUIFail(requestParams, new NetError(response.code(), NetError.HttpErrorCode.DATA_ERROR, "数据错误", e.getMessage()), requestParams.isSyn());
            }
            return;
        }
        if (requestParams.getDealMethod() != null) {
            //特殊处理
            CallBack callBack = requestParams.getDealMethod().dealCallBack(response.code(), json);
            if (callBack != null) {
                //失败
                if (callBack.getReturnCode() != 0) {
                    requestParams.getCallback().postUIFail(requestParams, new NetError(response.code(), NetError.HttpErrorCode.DATA_ERROR, callBack.getMsg(), null), requestParams.isSyn());
                    return;
                }
                if (callBack.getMsg() != null && !callBack.getMsg().equals("")) {
                    json = callBack.getMsg();
                }
            }

        }
        //httpcode 不是2开头的都是失败
        if (!String.valueOf(response.code()).startsWith("2")) {
            requestParams.getCallback().postUIFail(requestParams, new NetError(response.code(), NetError.HttpErrorCode.NET_DISCONNECT, json, null), requestParams.isSyn());
            return;
        }
        if (requestParams.getCallback() != null) {
            if (requestParams.getCallback().getType().equals(String.class)) {
                requestParams.getCallback().postUISuccess(json, requestParams.isSyn());
            } else if (requestParams.getCallback().getType().equals(Object.class)) {
                requestParams.getCallback().postUISuccess(json, requestParams.isSyn());
            } else {
                if (requestParams.getCallback().getType() == null) {
                    requestParams.getCallback().postUISuccess(JSONObject.parseObject(json, String.class), requestParams.isSyn());
                } else {
                    requestParams.getCallback().postUISuccess(JSONObject.parseObject(json, requestParams.getCallback().getType()), requestParams.isSyn());
                }
            }
        }

    }

    /**
     * 文件上传跟加载
     */
    private void postLoadSuccess(Call call, Response response, String str, RequestParams requestParams) {
        if (requestParams.getDealMethod() != null) {
            //特殊处理
            CallBack callBack = requestParams.getDealMethod().dealCallBack(response.code(), str);
            if (callBack != null) {
                if (callBack.getReturnCode() != 0) {
                    requestParams.getCallback().postUIFail(requestParams, new NetError(response.code(), NetError.HttpErrorCode.DATA_ERROR, callBack.getMsg(), null), requestParams.isSyn());
                    return;
                }
                if (callBack.getMsg() != null && !callBack.getMsg().equals("")) {
                    str = callBack.getMsg();
                }
            }

        }
        if (!String.valueOf(response.code()).startsWith("2")) {
            requestParams.getCallback().postUIFail(requestParams, new NetError(response.code(), NetError.HttpErrorCode.NET_DISCONNECT, str, null), requestParams.isSyn());
            return;
        }
        if (requestParams.getCallback() != null) {
            if (requestParams.getCallback().getType().equals(String.class)) {
                requestParams.getCallback().postUISuccess(str, requestParams.isSyn());
            } else if (requestParams.getCallback().getType().equals(Object.class)) {
                requestParams.getCallback().postUISuccess(str, requestParams.isSyn());
            } else {
                if (requestParams.getCallback().getType() == null) {
                    requestParams.getCallback().postUISuccess(JSONObject.parseObject(str, String.class), requestParams.isSyn());
                } else {
                    requestParams.getCallback().postUISuccess(JSONObject.parseObject(str, requestParams.getCallback().getType()), requestParams.isSyn());
                }
            }
        }
    }


}
