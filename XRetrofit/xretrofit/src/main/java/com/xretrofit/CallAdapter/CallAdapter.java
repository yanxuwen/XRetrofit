package com.xretrofit.CallAdapter;

import com.xretrofit.call.Call;
import com.xretrofit.utils.Utils;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

/**
 * @author bsnl_yanxuwen
 * @date 2021/2/4 16:02
 * Description :
 * 适配器
 *
 * R  为  封装后的类型。
 * T  为接口返回类型
 */
public interface CallAdapter<R, T>{
    R adapt(Call<T> call);

    /**
     * 接口数据 类型
     */
    Type responseType();

    abstract class Factory {
        /**
         * 封装转换器，如封装Observable
         */
        public abstract @Nullable CallAdapter<?, ?> get(Type returnType);

        protected static Class<?> getRawType(Type type) {
            return Utils.getRawType(type);
        }
    }
}
