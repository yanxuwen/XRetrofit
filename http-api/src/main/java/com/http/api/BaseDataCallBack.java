package com.http.api;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.Serializable;
import java.lang.reflect.Type;

import okhttp3.Call;


public abstract class BaseDataCallBack<T> implements Serializable {

    private Type mType = String.class;

    private Handler mHandler;

    private int retry = 1;//重试次数

    protected boolean isCallBack = true;//是否回调

    public Handler getHandler() {
        return mHandler;
    }

    public BaseDataCallBack(Class<T> clazz) {
        mType = clazz;
        mHandler = new Handler(Looper.getMainLooper());
    }

    public BaseDataCallBack() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    public abstract void onHttpSuccess(T result);

    public abstract void onHttpFail(NetError netError);

    public void onHttpStart(final Call call) {

    }

    /**
     * @return 是否切换到UI线程，默认是UI线程
     */
    public boolean isUI() {
        return true;
    }

    /**
     * 成功-回调到UI线程
     *
     * @param t
     */
    public final void postUISuccess(final T t, boolean syn) {
        if (!isCallBack) {
            return;
        }
        if (syn || !isUI()) {
            onHttpSuccess(t);
        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (!isCallBack) {
                            return;
                        }
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
    protected final void postUIFail(Call call,int retry,final NetError netError, boolean syn) {
        if (!isCallBack) {
            return;
        }
        if (call != null && this.retry < retry){
            //重新请求
            this.retry ++;
            Log.e("yxw","重试");
            //这个不能用
            call.request();
            return;
        }
        if (syn || !isUI()) {
            onHttpFail(netError);
        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (!isCallBack) {
                            return;
                        }
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
    public void postUIStart(final Call call) {
        if (!isCallBack) {
            return;
        }
        if (!isUI()) {
            onHttpStart(call);
        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (!isCallBack) {
                            return;
                        }
                        onHttpStart(call);
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