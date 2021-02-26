package com.xretrofit.converter;

import okhttp3.ResponseBody;

/**
 * @author bsnl_yanxuwen
 * @date 2021/2/4 17:09
 * Description :
 * 接口数据转换
 * 返回接口的   字符串操作
 */
public class StringResponseConverter<T> implements Converter<ResponseBody, T> {


    @Override
    public T convert(ResponseBody value) throws Exception {
        String responseStr = value.string();
        return (T) responseStr;
    }
}
