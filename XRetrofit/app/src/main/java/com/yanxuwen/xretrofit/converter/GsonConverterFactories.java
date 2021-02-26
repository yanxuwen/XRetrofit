package com.yanxuwen.xretrofit.converter;

import com.xretrofit.converter.Converter;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * @author bsnl_yanxuwen
 * @date 2021/2/4 15:46
 * Description :
 * 接口数据转换器
 * fastjson转换器
 */
public class GsonConverterFactories extends Converter.Factory {

    public static GsonConverterFactories create() {
        return new GsonConverterFactories();
    }

    @Nullable
    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type request, Type requestParamType) {
        if (requestParamType instanceof Object) {
            return new GsonRequestConverter();
        }
        return null;
    }

    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type request, Type responseType) {
        Class<?> rawType = getRawType(responseType);
        if (rawType instanceof Object) {
            return new GsonResponseConverter(responseType);
        }
        return null;
    }
}
