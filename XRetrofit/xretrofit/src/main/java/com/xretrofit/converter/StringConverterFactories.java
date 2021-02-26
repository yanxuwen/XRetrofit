package com.xretrofit.converter;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

import okhttp3.ResponseBody;

/**
 * @author bsnl_yanxuwen
 * @date 2021/2/4 15:46
 * Description :
 * 接口数据转换器
 * 字符串转换器
 */
public class StringConverterFactories extends Converter.Factory {

    public static StringConverterFactories create() {
        return new StringConverterFactories();
    }


    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type request,Type responseType) {
        Class<?> rawType = getRawType(responseType);
        if (rawType == String.class) {
            return new StringResponseConverter();
        }
        return null;
    }
}
