package com.yanxuwen.xretrofit.converter;

import com.xretrofit.CallAdapter.CallAdapter;
import com.xretrofit.utils.Utils;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

import io.reactivex.Observable;

/**
 * @author bsnl_yanxuwen
 * @date 2021/2/4 15:46
 * Description :
 * rxjava2 适配器
 */
public class Rxjava2CallAdapterFactories<T> extends CallAdapter.Factory {

    public static Rxjava2CallAdapterFactories create() {
        return new Rxjava2CallAdapterFactories();
    }


    @Nullable
    @Override
    public CallAdapter<Observable, T> get(Type returnType) {
        Class<?> rawType = getRawType(returnType);
        final Type responseType = Utils.getCallResponseType(returnType);
        if (rawType == Observable.class) {
            return new Rxjava2CallAdapter<T>(responseType);
        }
        return null;
    }
}
