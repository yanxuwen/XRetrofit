package com.http.api;


import java.lang.reflect.Type;

public abstract class DataCallBack<T> {

    private Type mType;


    public DataCallBack(Class<T> clazz) {
        mType = clazz;
    }


    public abstract void onHttpSuccess(T result);
    public abstract void onHttpFail(NetError netError);
    /**
     * 成功-回调到UI线程
     *
     * @param t
     */
    public void postUISuccess(final T t,boolean syn) {
        onHttpSuccess(t);
    }

    /**
     * 失败-回调到UI线程
     */
    public void postUIFail(final NetError netError,boolean syn) {
        onHttpFail(netError);
    }

    public Type getType() {
        return mType;
    }
}
