package com.xretrofit.callback;

public abstract class ProgressCallBack<T> implements CallBack<T> {

    /**
     * 进度
     */
    public abstract void onProgress(float progress);
}
