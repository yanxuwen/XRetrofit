package com.xretrofit.method;

import android.util.Log;
import android.util.Patterns;

import com.xretrofit.CallAdapter.CallAdapter;
import com.xretrofit.HttpManager;
import com.xretrofit.Interceptor.DownloadResponseBody;
import com.xretrofit.Interceptor.ProgressListener;
import com.xretrofit.Interceptor.UploadRequestBodyBody;
import com.xretrofit.bean.RequestParams;
import com.xretrofit.call.Call;
import com.xretrofit.call.OkHttpCall;
import com.xretrofit.call.ProgressCall;
import com.xretrofit.converter.Converter;
import com.xretrofit.utils.UrlUtils;
import com.xretrofit.utils.Utils;
import com.yanxuwen.xretrofit_annotations.annotation.method.DELETE;
import com.yanxuwen.xretrofit_annotations.annotation.method.DOWNLOAD;
import com.yanxuwen.xretrofit_annotations.annotation.method.FORM;
import com.yanxuwen.xretrofit_annotations.annotation.method.GET;
import com.yanxuwen.xretrofit_annotations.annotation.method.Headers;
import com.yanxuwen.xretrofit_annotations.annotation.method.POST;
import com.yanxuwen.xretrofit_annotations.annotation.method.PUT;
import com.yanxuwen.xretrofit_annotations.annotation.method.UPLOAD;
import com.yanxuwen.xretrofit_annotations.annotation.param.Body;
import com.yanxuwen.xretrofit_annotations.annotation.param.FilePath;
import com.yanxuwen.xretrofit_annotations.annotation.param.Header;
import com.yanxuwen.xretrofit_annotations.annotation.param.Param;
import com.yanxuwen.xretrofit_annotations.annotation.param.Path;
import com.yanxuwen.xretrofit_annotations.annotation.param.Query;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ServiceMethod {

    //提交json数据
    private static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
    //提交字符串数据
    private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown;charset=utf-8");

    private RequestParams requestParams = new RequestParams();

    /**
     * 头部暂时未处理
     */
    public <R> R request(Map<Class, MethodAnnotation> mapMa, List<ParamAnnotation> listPA, Type returnType) {
        if (listPA == null) {
            return null;
        }
        buildHeaders(mapMa,listPA);
        Call<Object> myCall = null;
        Type requestType = null;
        if (mapMa.containsKey(GET.class)) {
            Class annotation = GET.class;
            requestType = annotation;
            buildUrl(mapMa.get(annotation).getKey(), listPA);
            final Request request = buildRequest(annotation, null);
            okhttp3.Call call = HttpManager.getInstance().callFactory().newCall(request);
            myCall = new OkHttpCall<>(call);
        } else if ((mapMa.containsKey(POST.class) || mapMa.containsKey(PUT.class) || mapMa.containsKey(DELETE.class)) && mapMa.containsKey(FORM.class)) {
            Class annotation = POST.class;
            if (mapMa.containsKey(POST.class)) {
                annotation = mapMa.get(POST.class).getAnnotation();
            } else if (mapMa.containsKey(PUT.class)) {
                annotation = mapMa.get(PUT.class).getAnnotation();
            }
            if (mapMa.containsKey(DELETE.class)) {
                annotation = mapMa.get(DELETE.class).getAnnotation();
            }
            requestType = annotation;
            //表单提交
            buildUrl(mapMa.get(annotation).getKey(), listPA);
            Map<String, Object> params = new HashMap<>();
            for (ParamAnnotation paramAnnotation : listPA) {
                if (Param.class == paramAnnotation.getAnnotation()) {
                    params.put(paramAnnotation.getKey(), paramAnnotation.getValue());
                }
            }
            FormBody.Builder builder = new FormBody.Builder();
            addMapParmsToFromBody(params, builder);
            Request request = buildRequest(annotation, builder.build());
            okhttp3.Call call = HttpManager.getInstance().callFactory().newCall(request);
            myCall = new OkHttpCall<>(call);
        } else if (mapMa.containsKey(POST.class) || mapMa.containsKey(PUT.class) || mapMa.containsKey(DELETE.class)) {
            Class annotation = POST.class;
            if (mapMa.containsKey(POST.class)) {
                annotation = mapMa.get(POST.class).getAnnotation();
            } else if (mapMa.containsKey(PUT.class)) {
                annotation = mapMa.get(PUT.class).getAnnotation();
            }
            if (mapMa.containsKey(DELETE.class)) {
                annotation = mapMa.get(DELETE.class).getAnnotation();
            }
            requestType = annotation;
            //json提交
            buildUrl(mapMa.get(annotation).getKey(), listPA);
            String json;
            JSONObject jb = new JSONObject();
            RequestBody requestBody = null;
            for (ParamAnnotation paramAnnotation : listPA) {
                if (Param.class == paramAnnotation.getAnnotation()) {
                    try {
                        jb.put(paramAnnotation.getKey(), paramAnnotation.getValue());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    json = jb.toString();
                    requestBody = RequestBody.create(JSON, json);
                } else if (Body.class == paramAnnotation.getAnnotation() && paramAnnotation.getValue() instanceof String) {
                    requestBody = RequestBody.create(JSON, (String) paramAnnotation.getValue());
                } else {
                    Converter<Object, RequestBody> converter = createRequestConverter(annotation, paramAnnotation.getValue().getClass());
                    try {
                        requestBody = converter.convert(paramAnnotation.getValue());
                    } catch (Exception e) {
                        //实体转换，使用转换器
                        throw new IllegalStateException("@Body 没有定义该" + paramAnnotation.getValue().getClass().getSimpleName() + "的转换器，请使用addConverterFactory添加转换器");
                    }
                }
            }
            if (requestBody == null) {
                throw new NullPointerException("requestBody 为空");
            }
            Request request = buildRequest(annotation, requestBody);
            okhttp3.Call call = HttpManager.getInstance().callFactory().newCall(request);
            myCall = new OkHttpCall<>(call);
        } else if (mapMa.containsKey(DOWNLOAD.class)) {
            Class annotation = DOWNLOAD.class;
            requestType = annotation;
            buildUrl(mapMa.get(annotation).getKey(), listPA);
            String filePath = null;
            for (ParamAnnotation paramAnnotation : listPA) {
                //下载只支持String,File类型，因为他还支持List,后续看下是不是支持跟@body一样可以自定义
                if (FilePath.class == paramAnnotation.getAnnotation()) {
                    if (paramAnnotation.getValue() instanceof String) {
                        filePath = (String) paramAnnotation.getValue();
                    } else if (paramAnnotation.getValue() instanceof File) {
                        filePath = ((File) paramAnnotation.getValue()).getAbsolutePath();
                    }
                }
            }
            if (filePath == null || filePath.equals("")) {
                throw new IllegalStateException("文件路径不存在");
            }
            final File file = new File(filePath);
            File folder = file.getParentFile();
            if (!folder.exists()) {
                try {
                    boolean isMkdirs = folder.mkdirs();
                    if (!isMkdirs) {
                        throw new FileNotFoundException("java.io.FileNotFoundException: " + folder + ": open failed: ENOENT (No such file or directory)");
                    }
                } catch (Exception e) {
                    throw new IllegalStateException("文件创建失败");
                }
            }
            final long startsPoint = file.length() > 0 ? file.length() - 1 : file.length();
            requestParams.addHeader("RANGE", "bytes=" + startsPoint + "-");//断点续传
            Request request = buildRequest(annotation, null);
            final ProgressListener listener = new ProgressListener();

            // 重写ResponseBody监听请求
            Interceptor interceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Response originalResponse = chain.proceed(chain.request());
                    DownloadResponseBody responseBody = new DownloadResponseBody(originalResponse, startsPoint, file);
                    responseBody.setProgressListener(listener);
                    return originalResponse.newBuilder()
                            .body(responseBody)
                            .build();
                }
            };
            okhttp3.Call call = HttpManager.getInstance().getOkHttpDownloadClient(interceptor).newCall(request);
            myCall = new ProgressCall<>(call, listener);
        } else if (mapMa.containsKey(UPLOAD.class)) {
            Class annotation = UPLOAD.class;
            requestType = annotation;
            buildUrl(mapMa.get(annotation).getKey(), listPA);
            List<File> listPath = new ArrayList<>();
            for (ParamAnnotation paramAnnotation : listPA) {
                if (FilePath.class == paramAnnotation.getAnnotation()) {
                    if (paramAnnotation.getValue() instanceof String) {
                        listPath.add(new File((String) paramAnnotation.getValue()));
                    } else if (paramAnnotation.getValue() instanceof File) {
                        listPath.add(((File) paramAnnotation.getValue()));
                    } else if (paramAnnotation.getValue() instanceof List) {
                        for (Object o : (List) paramAnnotation.getValue()) {
                            if (o instanceof String) {
                                listPath.add(new File((String) o));
                            } else if (o instanceof File) {
                                listPath.add(((File) o));
                            }
                        }
                    }
                }
            }
            //1.构建MultipartBody
            MultipartBody.Builder multipartBody = new MultipartBody.Builder();
            multipartBody.setType(MultipartBody.FORM);
            //添加额外参数
            for (ParamAnnotation paramAnnotation : listPA) {
                if (Param.class == paramAnnotation.getAnnotation()) {
                    try {
                        multipartBody.addFormDataPart(paramAnnotation.getKey(), String.valueOf(paramAnnotation.getValue()));//后台接收文件流的参数名
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            //2.循环创建多个RequestBody
            RequestBody fileBody = null;
            ProgressListener[] listeners = new ProgressListener[listPath.size()];
            for (int i = 0; i < listPath.size(); i++) {
                File file = listPath.get(i);
                String fileName = file.getName();

                fileBody = RequestBody.create(MediaType.parse(Utils.guessMimeType(fileName)), file);
                UploadRequestBodyBody requestBody = new UploadRequestBodyBody(fileBody);
                listeners[i] = new ProgressListener();
                requestBody.setProgressListener(listeners[i]);
                //3.MultipartBody 添加Part 值
                multipartBody.addFormDataPart("file", fileName, requestBody);//后台接收文件流的参数名
            }
            final Request request = buildRequest(annotation, multipartBody.build());
            okhttp3.Call call = HttpManager.getInstance().callFactory().newCall(request);
            myCall = new ProgressCall<>(call, listeners);
        }


        return adapt(requestType, myCall, returnType);
    }

    private void buildHeaders(Map<Class, MethodAnnotation> mapMa, List<ParamAnnotation> listPA){
        Map<String,String> mapHeader = new HashMap<>();
         if (mapMa.containsKey(Headers.class)){
             List<Object> list = mapMa.get(Headers.class).getKeys();
             if (list != null) {
                 for (Object o: list){
                     String[] header = String.valueOf(o).split(":");
                     if (header == null || header.length<2){
                         continue;
                     }
                     mapHeader.put(header[0],header[1]);
                 }
             }
         }
         for (ParamAnnotation paramAnnotation : listPA){
             if (Header.class == paramAnnotation.getAnnotation()){
                 mapHeader.put(paramAnnotation.getKey(),String.valueOf(paramAnnotation.getValue()));
             }
         }
         requestParams.setHeaders(mapHeader);
    }

    /**
     * 编辑参数
     */
    private void buildUrl(String url, List<ParamAnnotation> listPA) {
        if (listPA == null) {
            return;
        }
        Map<String, Object> urlJoint = new HashMap<>();

        for (ParamAnnotation paramAnnotation : listPA) {
            if (Query.class == paramAnnotation.getAnnotation()) {
                urlJoint.put(paramAnnotation.getKey(), paramAnnotation.getValue());
            } else if (Path.class == paramAnnotation.getAnnotation() && paramAnnotation.getValue() instanceof String) {
                url = url.replace("{" + paramAnnotation.getKey() + "}", (String) paramAnnotation.getValue());
            }
        }

        url = UrlUtils.urlJoint(url, urlJoint);
        if (!Patterns.WEB_URL.matcher(url).matches() && HttpManager.getInstance().baseUrl != null) {
          url = HttpManager.getInstance().baseUrl + url;
        }
        Log.e("yxw","url: " + url);
        requestParams.setUrl(url);
    }

    private void addMapParmsToFromBody(Map<String, Object> params, FormBody.Builder builder) {
        for (Map.Entry<String, Object> map : params.entrySet()) {
            String key = map.getKey();
            String value = "";
            /**
             * 判断值是否是空的
             */
            if (map.getValue() == null) {
                value = "";
            } else if (map.getValue() instanceof String) {
                value = (String) map.getValue();
            }
            /**
             * 把key和value添加到formbody中
             */
            builder.add(key, value);
        }
    }

    private Request buildRequest(Class annotation, RequestBody body) {
        Request.Builder builder = new Request.Builder();
        builder.url(requestParams.getUrl());
        if (body != null) {
            if (POST.class == annotation || UPLOAD.class == annotation) {
                builder.post(body);
            } else if (PUT.class == annotation) {
                builder.put(body);
            } else if (DELETE.class == annotation) {
                builder.delete(body);
            }
        } else if (GET.class == annotation) {
            builder.get();
        }
        if (requestParams.getHeaders() != null) {
            for (Map.Entry<String, String> entry : requestParams.getHeaders().entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        return builder.build();
    }


    /**
     * 获取请求参数数据转换器
     */
    private <T> Converter<T, RequestBody> createRequestConverter(Type request, Type requestParamType) {
        for (int i = 0; i < HttpManager.getInstance().converterFactories.size(); i++) {
            Converter<T, RequestBody> converter = (Converter<T, RequestBody>) HttpManager.getInstance().converterFactories.get(i).requestBodyConverter(request, requestParamType);
            if (converter != null) {
                return converter;
            }
        }
        return null;
    }

    /**
     * 获取结果数据转换器
     */
    public <T> Converter<ResponseBody, T> createResponseConverter(Type request, Type responseType) {
        for (int i = 0; i < HttpManager.getInstance().converterFactories.size(); i++) {
            Converter<ResponseBody, T> converter = (Converter<ResponseBody, T>) HttpManager.getInstance().converterFactories.get(i).responseBodyConverter(request, responseType);
            if (converter != null) {
                return converter;
            }
        }
        return null;
    }


    /**
     * 获取适配器
     */
    private CallAdapter<?, ?> createCallAdapter(Type type) {
        for (int i = 0; i < HttpManager.getInstance().callAdapterFactories.size(); i++) {
            CallAdapter<?, ?> converter = HttpManager.getInstance().callAdapterFactories.get(i).get(type);
            if (converter != null) {
                return converter;
            }
        }
        return null;
    }

    /**
     * 执行转化
     */
    private <R> R adapt(Type request, Call call, Type returnType) {
        CallAdapter<?, ?> callAdapter = createCallAdapter(returnType);
        call.init(createResponseConverter(request, callAdapter.responseType()));
        if (callAdapter != null) {
            return (R) callAdapter.adapt(call);
        }
        return null;
    }
}
