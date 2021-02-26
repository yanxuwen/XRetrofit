package com.xretrofit.call;

import com.xretrofit.Interceptor.ProgressListener;
import com.xretrofit.callback.CallBack;
import com.xretrofit.callback.ProgressCallBack;

import java.text.DecimalFormat;

import okhttp3.Call;

/**
 * @author bsnl_yanxuwen
 * @date 2021/2/7 17:05
 * Description :
 * 下载
 */
public class ProgressCall<T> extends OkHttpCall<T> {

    final DecimalFormat df = new DecimalFormat("#.00");

    private ProgressListener[] listeners;

    /**
     * @param listeners 用于监听上传跟下载的时候用的
     */
    public ProgressCall(Call call, ProgressListener... listeners) {
        super(call);
        this.listeners = listeners;
    }

    @Override
    public void enqueue(final CallBack<T> callback) {
        super.enqueue(callback);
        if (callback instanceof ProgressCallBack) {
            final ProgressCallBack progressCallBack = (ProgressCallBack) callback;
            if (listeners != null) {
                final float[] totalProgress = new float[listeners.length];
                for (int i = 0; i < listeners.length; i++) {
                    final int finalI = i;
                    listeners[i].setProgressListener(new ProgressListener.Listener() {
                        @Override
                        public void progress(float progress) {
                            totalProgress[finalI] = progress;
                            double total = 0;
                            for (double mProcess : totalProgress) {
                                total += mProcess;
                            }
                            progressCallBack.onProgress(Float.valueOf(df.format(total / listeners.length)));
                        }
                    });
                }
            }
        }
    }
}
