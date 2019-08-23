package com.http.api;

public abstract class ProgressCallBack<T> extends BaseDataCallBack<T>{

    public ProgressCallBack() {
        super();
     }

    public ProgressCallBack(Class<T> clazz) {
        super(clazz);
    }
    /**
     *  进度
     */
    public abstract void onLoadProgress(float progress);



    /**
     * 下载中-回调到UI线程
     */
    public final void postUILoading(final float progress , boolean syn) {
        if (syn){
            onLoadProgress(progress);
        } else {
            getHandler().post(new Runnable() {
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
}
