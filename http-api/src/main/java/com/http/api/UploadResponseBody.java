package com.http.api;

import java.io.IOException;
import java.text.DecimalFormat;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * 上传
 */
public class UploadResponseBody extends RequestBody {
    private RequestBody mRequestBody;
    private ProgressCallBack progressCallBack;
    private long mContentLength;
    final DecimalFormat df = new DecimalFormat("#.00");

    public UploadResponseBody(RequestBody requestBody, ProgressCallBack progressCallBack) {
        mRequestBody = requestBody;
        this.progressCallBack = progressCallBack;
    }

    //文件的总长度
    @Override
    public long contentLength() {
        try {
            if (mContentLength == 0)
                mContentLength = mRequestBody.contentLength();
            return mContentLength;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public MediaType contentType() {
        return mRequestBody.contentType();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        ByteSink byteSink = new ByteSink(sink);
        BufferedSink mBufferedSink = Okio.buffer(byteSink);
        mRequestBody.writeTo(mBufferedSink);
        mBufferedSink.flush();
    }


    private final class ByteSink extends ForwardingSink {
        //已经上传的长度
        private long mByteLength = 0L;

        ByteSink(Sink delegate) {
            super(delegate);
        }

        @Override
        public void write(Buffer source, long byteCount) throws IOException {
            super.write(source, byteCount);
            mByteLength += byteCount;
            progressCallBack.onLoadProgress(Float.valueOf(df.format((Float.valueOf(mByteLength) /  contentLength()) * 100)));
        }
    }
}