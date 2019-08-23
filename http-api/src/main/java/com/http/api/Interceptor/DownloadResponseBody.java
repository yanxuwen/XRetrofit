package com.http.api.Interceptor;


import com.http.api.BaseDataCallBack;
import com.http.api.ProgressCallBack;

import java.io.IOException;
import java.text.DecimalFormat;

import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;

/**
 * 下载
 */
public class DownloadResponseBody extends ResponseBody {

    private Response originalResponse;
    private BaseDataCallBack downloadListener;
    private long oldPoint = 0;
    private long maxProgress = 0;
    private boolean syn;
    DecimalFormat df = new DecimalFormat("#.00");


    public DownloadResponseBody(Response originalResponse, long startsPoint, BaseDataCallBack downloadListener, boolean syn){
        this.originalResponse = originalResponse;
        this.downloadListener = downloadListener;
        this.oldPoint = startsPoint;
        this.syn = syn;
    }

    @Override
    public MediaType contentType() {
        return originalResponse.body().contentType();
    }

    @Override
    public long contentLength() {
        return originalResponse.body().contentLength();
    }

    public void setMaxProgress(long maxProgress){
        this.maxProgress = maxProgress;
    }

    @Override
    public BufferedSource source() {
        return Okio.buffer(new ForwardingSource(originalResponse.body().source()) {
            private long bytesReaded = 0;
            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                bytesReaded += bytesRead == -1 ? 0 : bytesRead;
                if (downloadListener != null && downloadListener instanceof ProgressCallBack) {
                    ((ProgressCallBack)downloadListener).postUILoading(Float.valueOf(df.format((Float.valueOf(bytesReaded+oldPoint) / maxProgress) * 100)),syn);
                }
                return bytesRead;
            }
        });
    }

}