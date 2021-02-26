package com.yanxuwen.xretrofit.callback;

import com.xretrofit.Response;
import com.xretrofit.call.Call;
import com.xretrofit.callback.ProgressCallBack;

public abstract class MyProgressCallBack<T> extends ProgressCallBack<T> {

    @Override
    public void onStart(Call<T> call) {

    }

    @Override
    public final void onSuccess(Call<T> call, Response<T> response) {
        if (response.errorBody() != null) {
            fail("网络异常");
            return;
        }
        success(response.body());

    }

    @Override
    public final void onFail(Call<T> call, Throwable e) {
        fail(ServerException.handleException(e));
    }


    public abstract void success(T t);

    public abstract void fail(String msg);

}
