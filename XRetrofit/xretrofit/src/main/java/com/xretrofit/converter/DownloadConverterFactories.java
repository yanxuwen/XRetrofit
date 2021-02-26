package com.xretrofit.converter;

import com.yanxuwen.xretrofit_annotations.annotation.method.DOWNLOAD;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

import okhttp3.ResponseBody;

/**
 * @author bsnl_yanxuwen
 * @date 2021/2/7 16:32
 * 接口数据转换器
 * 下载转换器
 */
public class DownloadConverterFactories extends Converter.Factory {

    public static DownloadConverterFactories create() {
        return new DownloadConverterFactories();
    }


    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type request, Type responseType) {
        if (request == DOWNLOAD.class) {
            return new DownloadResponseConverter();
        }
        return null;
    }
}
