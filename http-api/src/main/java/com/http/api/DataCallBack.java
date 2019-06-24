package com.http.api;


import android.os.Handler;
import android.os.Looper;

import java.lang.reflect.Type;

import okhttp3.Call;

public abstract class DataCallBack<T> {

    private Type mType = String.class;

    private Handler mHandler;

    public Handler getHandler() {
        return mHandler;
    }

    public DataCallBack(Class<T> clazz) {
        mType = clazz;
        mHandler = new Handler(Looper.getMainLooper());
    }

    public DataCallBack() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    public abstract void onHttpSuccess(T result);
    public abstract void onHttpFail(NetError netError);
    public void onHttpStart(Call call){}
    /**
     * 成功-回调到UI线程
     *
     * @param t
     */
    public final void postUISuccess(final T t,boolean syn) {
        if (syn){
            onHttpSuccess(t);
        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        onHttpSuccess(t);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * 失败-回调到UI线程
     */
    public final void postUIFail(final NetError netError,boolean syn) {
        if (syn){
            onHttpFail(netError);
        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        onHttpFail(netError);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }

    /**
     * 开始-回调到UI线程
     */
    public final void postUIStart(final Call call) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    onHttpStart(call);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Type getType() {
        return mType;
    }
}