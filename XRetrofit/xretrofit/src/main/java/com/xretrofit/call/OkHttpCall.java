package com.xretrofit.call;

import android.os.Handler;
import android.os.Looper;

import com.xretrofit.Response;
import com.xretrofit.callback.CallBack;
import com.xretrofit.converter.Converter;
import com.xretrofit.utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.BufferedSource;

/**
 * @author bsnl_yanxuwen
 * @date 2021/2/4 11:17
 * Description :
 */
public class OkHttpCall<T> implements Call<T> {
    protected Converter<ResponseBody, T> responseConverter;//结果转换器
    protected okhttp3.Call call;
    private volatile boolean canceled;
    final Handler mHandler = new Handler(Looper.getMainLooper());

    public OkHttpCall(okhttp3.Call call) {
        this.call = call;
    }

    @Override
    public void init(Converter<ResponseBody, T> responseConverter) {
        this.responseConverter = responseConverter;
    }

    /**
     * 执行请求，异步
     */
    @Override
    public void enqueue(final CallBack<T> callback) {
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull okhttp3.Call call, @NotNull final IOException e) {
                if (callback == null) {
                    return;
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFail(OkHttpCall.this, e);
                    }
                });
            }

            @Override
            public void onResponse(@NotNull okhttp3.Call call, @NotNull final okhttp3.Response response) throws IOException {
                if (callback == null) {
                    return;
                }
                final Response<T> tResponse;
                try {
                    if (canceled) {
                        return;
                    }
                    tResponse = parseResponse(response);
                } catch (final Exception e) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onFail(OkHttpCall.this, e);
                        }
                    });
                    return;
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess(OkHttpCall.this, tResponse);
                    }
                });
            }
        });
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onStart(OkHttpCall.this);
            }
        });
    }

    /**
     * 执行请求，同步
     */
    @Override
    public Response<T> execute() throws Exception {
        okhttp3.Response rawResponse = call.execute();
        if (canceled) {
            return null;
        }
        return parseResponse(rawResponse);
    }

    @Override
    public void cancel() {
        canceled = true;
        if (call != null) {
            call.cancel();
        }
    }

    @Override public boolean isCanceled() {
        if (canceled) {
            return true;
        }
        synchronized (this) {
            return call != null && call.isCanceled();
        }
    }

    @Override
    public Call<T> clone() {
        OkHttpCall okHttpCall = new OkHttpCall(call);
        okHttpCall.init(responseConverter);
        return okHttpCall;
    }

    /**
     * 结果请求，数据处理
     */
    protected Response<T> parseResponse(okhttp3.Response rawResponse) throws Exception {
        ResponseBody rawBody = rawResponse.body();
        // Remove the body's source (the only stateful object) so we can pass the response along.
        //删除主体的源（唯一有状态的对象），这样我们就可以传递响应。
        //抄袭 retrofit的
        rawResponse = rawResponse.newBuilder()
                .body(new NoContentResponseBody(rawBody.contentType(), rawBody.contentLength()))
                .build();

        //不是200的则错误信息
        int code = rawResponse.code();
        if (code < 200 || code >= 300) {
            try {
                // Buffer the entire body to avoid future I/O.
                // 缓冲整个主体以避免将来的I/O。
                ResponseBody bufferedBody = Utils.buffer(rawBody);
                return Response.error(bufferedBody, rawResponse);
            } finally {
                rawBody.close();
            }
        }
        //HTTP 204(no content)表示响应执行成功，但没有数据返回，浏览器不用刷新，不用导向新页面。
        //HTTP 205(reset content) 表示响应执行成功，重置页面（Form表单），方便用户下次输入。
        if (code == 204 || code == 205) {
            rawBody.close();
            return Response.success(null, rawResponse);
        }
        //使用转换器转换
        T body = null;
        if (responseConverter != null) {
            body = responseConverter.convert(rawBody);
        }
        rawBody.close();
        return Response.success(body, rawResponse);
    }


    static final class NoContentResponseBody extends ResponseBody {
        private final MediaType contentType;
        private final long contentLength;

        NoContentResponseBody(MediaType contentType, long contentLength) {
            this.contentType = contentType;
            this.contentLength = contentLength;
        }

        @Override
        public MediaType contentType() {
            return contentType;
        }

        @Override
        public long contentLength() {
            return contentLength;
        }

        @Override
        public BufferedSource source() {
            throw new IllegalStateException("Cannot read raw response body of a converted body.");
        }
    }
}
