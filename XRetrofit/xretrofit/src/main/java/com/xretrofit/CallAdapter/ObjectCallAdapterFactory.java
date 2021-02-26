package com.xretrofit.CallAdapter;

import com.xretrofit.call.Call;
import com.xretrofit.call.ProgressCall;
import com.xretrofit.utils.Utils;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

/**
 * @author bsnl_yanxuwen
 * @date 2021/2/4 17:47
 * Description :
 * 适配器Factory
 */
public class ObjectCallAdapterFactory extends CallAdapter.Factory {

    public static ObjectCallAdapterFactory create() {
        return new ObjectCallAdapterFactory();
    }


    @Nullable
    @Override
    public CallAdapter<?, ?> get(Type returnType) {
        Class<?> rawType = getRawType(returnType);
        final Type responseType = Utils.getCallResponseType(returnType);
        if (rawType == ProgressCall.class) {
            return new ProgressCallAdapter(responseType);
        } else if (rawType == Call.class) {
            return new MCallAdapter<>(responseType);
        }
        return new ObjectCallAdapter(responseType);
    }
}
