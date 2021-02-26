package com.xretrofit.CallAdapter;

import com.xretrofit.call.Call;
import com.xretrofit.call.ProgressCall;

import java.lang.reflect.Type;

/**
 * @author bsnl_yanxuwen
 * @date 2021/2/4 17:49
 * Description :
 * 返回类型为{@link ProgressCall<> 的适配器  带进度的
 */
public class ProgressCallAdapter<T> implements CallAdapter<ProgressCall,T> {

    private Type responseType;

    protected ProgressCallAdapter(Type responseType){
        this.responseType = responseType;
    }

    @Override
    public ProgressCall adapt(Call<T> call) {
        if (call != null){
            return (ProgressCall) call;
        }
        return null;
    }

    @Override
    public Type responseType() {
        return responseType;
    }



}
