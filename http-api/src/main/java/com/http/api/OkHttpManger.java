package com.http.api;

import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.ConnectException;
import java.net.FileNameMap;
import java.net.SocketTimeoutException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@SuppressWarnings("unchecked")
public class OkHttpManger {
    private OkHttpClient okHttpClient;
    private OkHttpClient.Builder builder;
    private volatile static OkHttpManger instance;
    private static long connectTimeout = 10 * 1000;
    private static long readTimeout = 10 * 1000;
    private static long writeTimeout = 10 * 1000;

    //提交json数据
    private static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
    //提交字符串数据
    private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown;charset=utf-8");

    private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
    // 使用getCacheDir()来作为缓存文件的存放路径（/data/data/包名/cache） ，
// 如果你想看到缓存文件可以临时使用 getExternalCacheDir()（/sdcard/Android/data/包名/cache）。
    private static File cacheFile;
    private static Cache cache;

    public OkHttpManger() {

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
        if (sslParams != null) {
            getBuilder().sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        }
        return this;
    }

    /**
     * 设置超时时间，单位毫秒
     */
    public OkHttpManger setTimeOut(long connectTimeout, long readTimeout, long writeTimeout) {
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
        this.writeTimeout = writeTimeout;
        return this;
    }

    public void setOkHttpClient(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    public OkHttpClient.Builder getBuilder() {
        if (builder == null) {
            builder = new OkHttpClient.Builder()
                    .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                    .readTimeout(readTimeout, TimeUnit.MILLISECONDS)
                    .writeTimeout(writeTimeout, TimeUnit.MILLISECONDS)
                    .cookieJar(new CookieJar() {
                        @Override
                        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                            cookieStore.put(url.host(), cookies);
                        }

                        @Override
                        public List<Cookie> loadForRequest(HttpUrl url) {
                            List<Cookie> cookies = cookieStore.get(url.host());
                            return cookies != null ? cookies : new ArrayList<Cookie>();
//自动管理Cookie发送Request都不用管Cookie这个参数也不用去response获取新Cookie什么的了。还能通过cookieStore获取当前保存的Cookie。
                        }
                    });
            SslUtils.SSLParams sslParams = SslUtils.getSslSocketFactory();
            builder.sslSocketFactory(SslUtils.getSslSocketFactory().sSLSocketFactory, sslParams.trustManager);
        }
        return builder;
    }

    public OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            okHttpClient = getBuilder().build();
        }
        return okHttpClient;
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
        return null;
    }

    /**
     * get异步请求不传参数
     * 通过response.body().string()获取返回的字符串
     * 异步返回值不能更新UI，要开启新线程
     *
     * @param url
     * @return
     */
    public void get(String url, Map<String, String> headers, final DataCallBack dataCallBack,boolean syn) {
        get(url, headers, null, dataCallBack,syn);
    }

    public void get(String url, Map<String, String> headers, final HttpDealMethod httpDealMethod, final DataCallBack dataCallBack, final boolean syn) {
        final Request request = buildRequest(url, null, headers);
        Call call = getOkHttpClient().newCall(request);
        try {
            // 请求加入调度
            call.enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    postUIFail(dataCallBack, e,syn);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        postUISuccess(dataCallBack,httpDealMethod, response.body().string(),syn);
                    } catch (Exception e) {
                        postUIFail(dataCallBack, e,syn);
                        e.printStackTrace();
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * post异步请求map传参
     * 通过response.body().string()获取返回的字符串
     * 异步返回值不能更新UI，要开启新线程
     *
     * @param url
     * @return
     */
    public void post(String url, Map<String, String> params, Map<String, String> headers, final DataCallBack dataCallBack,final boolean syn) {
        post(url, params, headers, null, dataCallBack,syn);
    }

    public void post(String url, Map<String, String> params, Map<String, String> headers, final HttpDealMethod httpDealMethod, final DataCallBack dataCallBack,final boolean syn) {
        RequestBody requestBody;
        if (params == null) {
            params = new HashMap<>();
        }
        FormBody.Builder builder = new FormBody.Builder();
        /**
         * 在这对添加的参数进行遍历
         */
        addMapParmsToFromBody(params, builder);

        requestBody = builder.build();
        String realURL = UrlUtils.urlJoint(url, null);
        //结果返回
        final Request request = buildRequest(realURL, requestBody, headers);

        Call call = getOkHttpClient().newCall(request);
        try {
            // 请求加入调度
            call.enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    postUIFail(dataCallBack, e,syn);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        postUISuccess(dataCallBack,httpDealMethod, response.body().string(),syn);

                    } catch (Exception e) {
                        postUIFail(dataCallBack, e,syn);
                        e.printStackTrace();
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * post异步请求json传参
     * 通过response.body().string()获取返回的字符串
     * 异步返回值不能更新UI，要开启新线程
     *
     * @param url
     * @return
     */
    public void post(String url, String json, Map<String, String> headers, final DataCallBack dataCallBack,final boolean syn) {
        post(url, json, headers, null, dataCallBack,syn);
    }

    public void post(String url, String json, Map<String, String> headers, final HttpDealMethod httpDealMethod, final DataCallBack dataCallBack,final boolean syn) {
        final String realURL = UrlUtils.urlJoint(url, null);
        final Request request = buildJsonPostRequest(realURL, json, headers);
        Call call = getOkHttpClient().newCall(request);
        try {
            // 请求加入调度
            call.enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    postUIFail(dataCallBack, e,syn);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        postUISuccess(dataCallBack,httpDealMethod, response.body().string(),syn);

                    } catch (Exception e) {
                        postUIFail(dataCallBack, e,syn);
                        e.printStackTrace();
                    }

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * post异步请求RequestBody传参
     * 通过response.body().string()获取返回的字符串
     * 异步返回值不能更新UI，要开启新线程
     *
     * @param url
     * @return
     */
    public void post(String url, RequestBody requestBody, Map<String, String> headers, final DataCallBack dataCallBack,final boolean syn) {
        post(url, requestBody, headers, null, dataCallBack,syn);
    }

    public void post(String url, RequestBody requestBody, Map<String, String> headers, final HttpDealMethod httpDealMethod, final DataCallBack dataCallBack,final boolean syn) {
        String realURL = UrlUtils.urlJoint(url, null);
        //结果返回
        final Request request = buildRequest(realURL, requestBody, headers);
        Call call = getOkHttpClient().newCall(request);
        try {
            // 请求加入调度
            call.enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    postUIFail(dataCallBack, e,syn);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        postUISuccess(dataCallBack,httpDealMethod, response.body().string(),syn);

                    } catch (Exception e) {
                        postUIFail(dataCallBack, e,syn);
                        e.printStackTrace();
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 基于http的文件上传（传入文件名和key）
     * 通过addFormDataPart
     *
     * @param url
     * @param dataCallBack 自定义回调接口
     *                     将file作为请求体传入到服务端.
     * @param fileKey      文件传入服务器的键"image"
     * @filePath: 文件路径
     */
    public void upLoadFile(String url, String filePath, String fileKey, Map<String, String> headers, final DataCallBack dataCallBack,final boolean syn) {
        File file = new File(filePath);
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(fileKey, filePath, fileBody)
                .build();
        final String realURL = UrlUtils.urlJoint(url, null);
        final Request request = buildRequest(realURL, requestBody, headers);

        Call call = getOkHttpClient().newCall(request);
        try {
            // 请求加入调度
            call.enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    postUIFail(dataCallBack, e,syn);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        postUISuccess(dataCallBack,null, response.body().string(),syn);
                    } catch (Exception e) {
                        postUIFail(dataCallBack, e,syn);
                        e.printStackTrace();
                    }

                }
            });
        } catch (Exception e) {
            postUIFail(dataCallBack, e,syn);
            e.printStackTrace();
        }
    }

    /**
     * 基于http的文件上传（传入文件数组和key）混合参数和文件请求
     * 通过addFormDataPart可以添加多个上传的文件
     *
     * @param url
     * @param dataCallBack 自定义回调接口
     *                     将file作为请求体传入到服务端.
     * @param files        上传的文件
     * @param fileKeys     上传的文件key集合
     */
    public void upLoadFile(String url, File[] files, String[] fileKeys, Map<String, String> params, Map<String, String> headers, final DataCallBack dataCallBack,final boolean syn) {
        if (params == null) {
            params = new HashMap<>();
        }
        final String realURL = UrlUtils.urlJoint(url, null);
        FormBody.Builder builder = new FormBody.Builder();
        addMapParmsToFromBody(params, builder);
        RequestBody requestBody = builder.build();
        MultipartBody.Builder multipartBody = new MultipartBody.Builder();
        multipartBody.setType(MultipartBody.ALTERNATIVE)
                .addPart(requestBody);

        if (files != null) {
            RequestBody fileBody = null;
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                String fileName = file.getName();
                fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
                //TODO 根据文件名设置contentType
                multipartBody.addPart(Headers.of("Content-Disposition",
                        "form-data; name=\"" + fileKeys[i] + "\"; filename=\"" + fileName + "\""),
                        fileBody);
            }

        }

        final Request request = buildRequest(realURL, multipartBody.build(), headers);

        Call call = getOkHttpClient().newCall(request);
        try {
            // 请求加入调度
            call.enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    postUIFail(dataCallBack, e,syn);
                }

                @Override
                public void onResponse(Call call, Response response) {
                    try {
                        postUISuccess(dataCallBack,null, response.body().string(),syn);

                    } catch (Exception e) {
                        e.printStackTrace();
                        postUIFail(dataCallBack, e,syn);
                    }

                }
            });

        } catch (Exception e) {
            postUIFail(dataCallBack, e,syn);
            e.printStackTrace();
        }
    }

    /**
     * 文件下载
     *
     * @param url          path路径
     * @param destFileDir  本地存储的文件夹路径
     * @param dataCallBack 自定义回调接口
     */
    private void downLoadFile(final String url, final String destFileDir, Map<String, String> headers, final DataCallBack dataCallBack,final boolean syn) {
        String realURL = UrlUtils.urlJoint(url, null);
        Request request = buildRequest(realURL, null, headers);
        Call call = getOkHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                postUIFail(dataCallBack, e,syn);

            }

            @Override
            public void onResponse(Call call, Response response) {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                is = response.body().byteStream();
                File file = new File(destFileDir, getFileName(url));
                try {
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                } catch (IOException e) {
                    postUIFail(dataCallBack, e,syn);
                    e.printStackTrace();
                } finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            postUIFail(dataCallBack, e,syn);
                            e.printStackTrace();
                        }
                    }
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            postUIFail(dataCallBack, e,syn);
                            e.printStackTrace();
                        }
                    }

                }
                postUISuccess(dataCallBack, null,response.body().toString(),syn);

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

    private Request buildRequest(String url, RequestBody body, Map<String, String> headers) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        if (body != null) {
            builder.post(body);
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
    private Request buildStringPostRequest(String url, String json, Map<String, String> headers) {
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_MARKDOWN, json);
        return buildRequest(url, requestBody, headers);
    }

    /**
     * Json_POST请求参数
     *
     * @param url  url
     * @param json json
     * @return requestBody
     */
    private Request buildJsonPostRequest(String url, String json, Map<String, String> headers) {
//        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        RequestBody requestBody = RequestBody.create(JSON, json);
        return buildRequest(url, requestBody, headers);
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

    private void postUIFail(final DataCallBack dataCallBack, Exception e,boolean syn) {
        if (dataCallBack != null) {
            if (e instanceof SocketTimeoutException) {
                //超时
                dataCallBack.postUIFail(new NetError(NetError.HttpErrorCode.NET_TIMEOUT, "请求超时", e.getMessage()),syn);
            } else if (e instanceof ConnectException) {
                dataCallBack.postUIFail(new NetError(NetError.HttpErrorCode.NET_DISCONNECT, "网络异常", e.getMessage()),syn);
            } else {
                dataCallBack.postUIFail(new NetError(NetError.HttpErrorCode.ERROR, "错误", e.getMessage()),syn);
            }
        }
    }

    private void postUISuccess(final DataCallBack dataCallBack, HttpDealMethod httpDealMethod, String json,boolean syn) {
        try {
            if (httpDealMethod != null){
                CallBack callBack = httpDealMethod.dealCallBack(json);
                if (callBack == null){
                    dataCallBack.postUIFail(new NetError(NetError.HttpErrorCode.DATA_ERROR, "数据错误", null),syn);
                    return;
                }
                if (callBack.getReturnCode() != 0){
                    dataCallBack.postUIFail(new NetError(NetError.HttpErrorCode.DATA_ERROR, callBack.getMsg(), null),syn);
                    return;
                }
                if (callBack.getMsg() != null && !callBack.getMsg().equals("")){
                    json = callBack.getMsg();
                }
            }
            if (dataCallBack != null) {
                if (dataCallBack.getType().equals(String.class)) {
                    dataCallBack.postUISuccess(json,syn);
                } else if (dataCallBack.getType().equals(Object.class)) {
                    dataCallBack.postUISuccess(json,syn);
                } else {
                    if (dataCallBack.getType() == null){
                        dataCallBack.postUISuccess(JSONObject.parseObject(json, String.class),syn);
                    } else {
                        dataCallBack.postUISuccess(JSONObject.parseObject(json, dataCallBack.getType()),syn);
                    }
                }
            }
        } catch (Exception e) {
            dataCallBack.postUIFail(new NetError(NetError.HttpErrorCode.DATA_ERROR, "数据错误", e.getMessage()),syn);
        }
    }

}
