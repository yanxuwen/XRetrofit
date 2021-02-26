package com.yanxuwen.xretrofit.converter;

import com.alibaba.fastjson.JSONObject;
import com.xretrofit.converter.Converter;

import java.lang.reflect.Type;

import okhttp3.ResponseBody;

/**
 * @author bsnl_yanxuwen
 * @date 2021/2/4 17:09
 * Description :
 * 请求结果数据转换
 * Gson操作
 */
public class GsonResponseConverter<T> implements Converter<ResponseBody, T> {

    private Type responseType;

    protected GsonResponseConverter(Type responseType){
        this.responseType = responseType;
    }


    @Override
    public T convert(ResponseBody value) throws Exception {
        String responseStr = value.string();
        return (T) JSONObject.parseObject(responseStr,responseType);
    }
}
