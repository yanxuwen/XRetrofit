package com.yanxuwen.xretrofit.converter;

import com.alibaba.fastjson.JSONObject;
import com.xretrofit.converter.Converter;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * @author bsnl_yanxuwen
 * @date 2021/2/5 16:00
 * Description :
 * 请求参数数据转换
 */
class GsonRequestConverter<T> implements Converter<T, RequestBody> {
    private static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");


    @Override
    public RequestBody convert(T value) throws Exception {
        RequestBody requestBody = RequestBody.create(JSON, JSONObject.toJSONString(value));
        return requestBody;
    }
}
