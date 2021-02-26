package com.xretrofit.converter;

import okhttp3.RequestBody;

/**
 * @author bsnl_yanxuwen
 * @date 2021/2/8 9:25
 * Description :
 * * 接口数据转换
 * * 返回接口的   上次操作
 */
public class UploadRequestConverter<T> implements Converter<T, RequestBody> {

    @Override
    public RequestBody convert(T value) throws Exception {
        return null;
    }
}
