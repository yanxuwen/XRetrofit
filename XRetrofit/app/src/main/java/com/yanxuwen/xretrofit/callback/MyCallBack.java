package com.yanxuwen.xretrofit.callback;

import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.xretrofit.Response;
import com.xretrofit.call.Call;
import com.xretrofit.callback.CallBack;

/**
 * @author bsnl_yanxuwen
 * @date 2021/2/5 11:19
 * Description :
 */
public abstract class MyCallBack<T> implements CallBack<T> {

    private FragmentActivity activity;

    public MyCallBack() {
    }

    public MyCallBack(FragmentActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onStart(Call<T> call) {
        if (activity != null) {
            activity.getLifecycle().addObserver(new LifecycleObserver() {
                @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
                public void onDestroy() {
                    Log.e("yxw", "????取消请求");
                    call.cancel();
                }

            });
        }
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
        if (call.isCanceled()){
            cancel();
        } else {
            fail(ServerException.handleException(e));
        }
    }


    public abstract void success(T t);

    public abstract void fail(String msg);

    public void cancel(){

    }

}
