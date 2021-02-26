package com.xretrofit.converter;

import com.yanxuwen.xretrofit_annotations.annotation.method.UPLOAD;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

import okhttp3.RequestBody;

/**
 * @author bsnl_yanxuwen
 * @date 2021/2/7 16:32
 * 接口数据转换器
 * 转换器
 */
public class UploadConverterFactories extends Converter.Factory {

    public static UploadConverterFactories create() {
        return new UploadConverterFactories();
    }

    @Nullable
    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type request, Type requestParamType) {
        if (request == UPLOAD.class) {
            return new UploadRequestConverter<>();
        }
        return null;
    }
}
