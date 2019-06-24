package com.http.api;

import android.os.Handler;
import android.os.Looper;

import java.lang.reflect.Type;

import okhttp3.Call;

public abstract class ProgressCallBack<T> {

    private Type mType = String.class;
    private Handler mHandler;

    public ProgressCallBack() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    public ProgressCallBack(Class<T> clazz) {
        mType = clazz;
        mHandler = new Handler(Looper.getMainLooper());
    }
    /**
     *  开始
     */
    public void onHttpStart(Call call){}
    /**
     *  进度
     */
    public abstract void onLoadProgress(float progress);
    /**
     *  完成
     * @param t   如果是下载，则返回路径,只能String类型
     *             如果是上传，返回的是接口数据
     */
    public abstract  void onHttpSuccess(final T t);
    /**
     *  失败
     */
    public abstract void onHttpFail(final NetError netError);


    /**
     * 成功-回调到UI线程
     */
    protected final void postUIComplete(final T t,boolean syn) {
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
    protected final void postUIFail(final NetError netError,boolean syn) {
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
    protected final void postUIStart(final Call call) {
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

    /**
     * 下载中-回调到UI线程
     */
    protected final void postUILoading(final float progress , boolean syn) {
        if (syn){
            onLoadProgress(progress);
        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        onLoadProgress(progress);
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
