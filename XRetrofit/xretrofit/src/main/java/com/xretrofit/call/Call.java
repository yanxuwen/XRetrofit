package com.xretrofit.call;

import com.xretrofit.Response;
import com.xretrofit.callback.CallBack;
import com.xretrofit.converter.Converter;

import okhttp3.ResponseBody;


/**
 * @author bsnl_yanxuwen
 * @date 2021/2/4 11:02
 * Description :
 *
 */
public interface Call<T> {

    /**
     * 初始化接口返回类型
     */
    void init(Converter<ResponseBody, T> responseConverter);

    /**
     * 执行请求，异步
     */
    void enqueue(CallBack<T> callback);


    /**
     * 执行请求同步
     */
    Response<T> execute() throws Exception;


    void cancel();

    boolean isCanceled();

    Call<T> clone();

}
