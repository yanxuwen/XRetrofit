package com.http.api;


import android.os.Handler;
import android.os.Looper;

import java.lang.reflect.Type;

public abstract class DataCallBack<T> {

    private Type mType;

    private Handler mHandler;

    public DataCallBack(Class<T> clazz) {
        mType = clazz;
        mHandler = new Handler(Looper.getMainLooper());
    }

    public DataCallBack() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    public abstract void onHttpSuccess(T result);
    public abstract void onHttpFail(NetError netError);
    /**
     * 成功-回调到UI线程
     *
     * @param t
     */
    public void postUISuccess(final T t,boolean syn) {
        if (syn){
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
    public void postUIFail(final NetError netError,boolean syn) {
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

    public Type getType() {
        return mType;
    }
}
