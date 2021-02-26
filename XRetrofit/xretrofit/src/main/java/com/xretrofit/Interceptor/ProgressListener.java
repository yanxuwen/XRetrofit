package com.xretrofit.Interceptor;

/**
 * @author bsnl_yanxuwen
 * @date 2021/2/8 9:45
 * Description :
 */
public class ProgressListener {
    public void setProgress(float progress) {
        if (listener != null) {
            listener.progress(progress);
        }
    }

    public interface Listener {
        public void progress(float progress);
    }

    private Listener listener;

    public void setProgressListener(Listener listener) {
        this.listener = listener;
    }

}
