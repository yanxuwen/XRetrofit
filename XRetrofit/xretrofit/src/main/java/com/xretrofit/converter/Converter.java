package com.xretrofit.converter;

import com.xretrofit.utils.Utils;
import com.yanxuwen.xretrofit_annotations.annotation.param.Body;
import com.yanxuwen.xretrofit_annotations.annotation.param.FilePath;
import com.yanxuwen.xretrofit_annotations.annotation.param.Header;
import com.yanxuwen.xretrofit_annotations.annotation.param.Param;
import com.yanxuwen.xretrofit_annotations.annotation.param.Path;
import com.yanxuwen.xretrofit_annotations.annotation.param.Query;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * @author bsnl_yanxuwen
 * @date 2021/2/4 14:07
 * Description :
 * 如果是请求
 */
public interface Converter<F, T> {
    T convert(F value) throws Exception;

    abstract class Factory {

        /**
         * 请求转换器,目前只支持{@link Body @Body} 注解
         * @param request get post
         * @param requestParamType  请求的参数类型
         * @return
         */
        public @Nullable Converter<?, RequestBody> requestBodyConverter(Type request, Type requestParamType) {
            return null;
        }

        /**
         * 请求转换器 ,支持
         * {@link Header @Header}
         * {@link Query @Query}
         * {@link Path @Path}
         * {@link FilePath @FilePath}
         * {@link Param @FilePath}
         * @param request  get post
         * @param requestParamType 请求的参数类型
         * @return
         */
        public @Nullable Converter<?, String> stringConverter(Type request,Type requestParamType) {
            return null;
        }

        /**
         * 结果转换器
         * @param request    get post
         * @param responseType 结果返回的参数类型
         * @return
         */
        public @Nullable Converter<ResponseBody, ?> responseBodyConverter(Type request,Type responseType) {
            return null;
        }

        protected static Class<?> getRawType(Type type) {
            return Utils.getRawType(type);
        }
    }
}
