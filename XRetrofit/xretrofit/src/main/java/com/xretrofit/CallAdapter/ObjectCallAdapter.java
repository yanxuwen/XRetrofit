package com.xretrofit.CallAdapter;

import com.xretrofit.call.Call;

import java.lang.reflect.Type;

/**
 * @author bsnl_yanxuwen
 * @date 2021/2/4 17:49
 * Description :
 * 同步
 * Object 适配器,
 */
public class ObjectCallAdapter<T> implements CallAdapter<Object,T> {

    private Type responseType;

    protected ObjectCallAdapter(Type responseType){
        this.responseType = responseType;
    }

    @Override
    public Type responseType() {
        return responseType;
    }

    @Override
    public Object adapt(Call call) {
        try {
            return call.execute().body();
        } catch (Exception e) {
            return null;
        }
    }

}
