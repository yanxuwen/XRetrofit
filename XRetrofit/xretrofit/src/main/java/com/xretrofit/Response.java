package com.xretrofit;


import org.jetbrains.annotations.Nullable;

import okhttp3.Headers;
import okhttp3.ResponseBody;

import static com.xretrofit.utils.Utils.checkNotNull;


/**
 * @author bsnl_yanxuwen
 * @date 2021/2/4 11:11
 * Description :
 * 自己定义的Response，里面嵌套 okhttp的Response，可供后续替换
 */
public final class Response<T> {
    private okhttp3.Response rawResponse;
    private @Nullable
    T body;
    private @Nullable
    ResponseBody errorBody;

    private Response(okhttp3.Response rawResponse, @Nullable T body,
                     @Nullable ResponseBody errorBody) {
        this.rawResponse = rawResponse;
        this.body = body;
        this.errorBody = errorBody;
    }


    public static <T> Response<T> success(@Nullable T body, okhttp3.Response rawResponse) {
        checkNotNull(rawResponse, "rawResponse == null");
        if (!rawResponse.isSuccessful()) {
            throw new IllegalArgumentException("rawResponse must be successful response");
        }
        return new Response<>(rawResponse, body, null);
    }


    /**
     * Create an error response from {@code rawResponse} with {@code body} as the error body.
     */
    public static <T> Response<T> error(ResponseBody body, okhttp3.Response rawResponse) {
        checkNotNull(body, "body == null");
        checkNotNull(rawResponse, "rawResponse == null");
        if (rawResponse.isSuccessful()) {
            throw new IllegalArgumentException("rawResponse should not be successful response");
        }
        return new Response<>(rawResponse, null, body);
    }

    /** The raw response from the HTTP client. */
    public okhttp3.Response raw() {
        return rawResponse;
    }

    /** HTTP status code. */
    public int code() {
        return rawResponse.code();
    }

    /** HTTP status message or null if unknown. */
    public String message() {
        return rawResponse.message();
    }

    /** HTTP headers. */
    public Headers headers() {
        return rawResponse.headers();
    }

    /** Returns true if {@link #code()} is in the range [200..300). */
    public boolean isSuccessful() {
        return rawResponse.isSuccessful();
    }

    /** The deserialized response body of a {@linkplain #isSuccessful() successful} response. */
    public @Nullable T body() {
        return body;
    }

    /** The raw response body of an {@linkplain #isSuccessful() unsuccessful} response. */
    public @Nullable ResponseBody errorBody() {
        return errorBody;
    }

    @Override public String toString() {
        return rawResponse.toString();
    }
}
