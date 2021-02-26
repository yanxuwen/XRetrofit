package com.xretrofit.callback;

import com.xretrofit.call.Call;
import com.xretrofit.Response;


public interface CallBack<T> {

    void onStart(Call<T> call);

    void onSuccess(Call<T> call, Response<T> response);

    void onFail(Call<T> call, final Throwable e);
}