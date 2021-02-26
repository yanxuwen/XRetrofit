package com.xretrofit.CallAdapter;

import com.xretrofit.call.Call;

import java.lang.reflect.Type;

/**
 * @author bsnl_yanxuwen
 * @date 2021/2/4 17:49
 * Description :
 * 返回类型为Call<> 的适配器
 */
public class MCallAdapter<T> implements CallAdapter<Call,T> {

    private Type responseType;

    protected MCallAdapter(Type responseType){
        this.responseType = responseType;
    }


    @Override
    public Type responseType() {
        return responseType;
    }


    @Override
    public Call adapt(Call<T> call) {
        return call;
    }

}
