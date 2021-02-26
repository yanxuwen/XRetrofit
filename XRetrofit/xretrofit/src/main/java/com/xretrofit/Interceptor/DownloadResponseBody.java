package com.xretrofit.Interceptor;


import java.io.File;
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
    private long startsPoint = 0;
    private long maxProgress = 0;
    private File file;
    DecimalFormat df = new DecimalFormat("#.00");

    private ProgressListener progressListener;

    public void setProgressListener(ProgressListener progressListener) {
        this.progressListener = progressListener;
    }

    public DownloadResponseBody(Response originalResponse, long startsPoint, File file) {
        this.originalResponse = originalResponse;
        this.startsPoint = startsPoint;
        this.file = file;
    }

    @Override
    public MediaType contentType() {
        return originalResponse.body().contentType();
    }

    @Override
    public long contentLength() {
        return originalResponse.body().contentLength();
    }

    public void setMaxProgress(long maxProgress) {
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
                if (progressListener != null) {
                    progressListener.setProgress(Float.valueOf(df.format((Float.valueOf(bytesReaded+startsPoint) / maxProgress) * 100)));
                }
                return bytesRead;
            }
        });
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public long getStartsPoint() {
        return startsPoint;
    }
}