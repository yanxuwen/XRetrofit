package com.yanxuwen.xretrofit.callback;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.io.NotSerializableException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;

public class ServerException {

    public static  String handleException(Throwable e) {
        String msg = e.getMessage();
        if (e instanceof SocketTimeoutException) {
            msg = "网络连接超时，请检查网络";
        } else if (e instanceof ConnectException) {
            msg = "网络异常，请检查网络";
        } else if (e instanceof ConnectTimeoutException) {
            msg = "网络连接超时，请检查网络";
        } else if (e instanceof UnknownHostException) {
            msg = "网络异常，请检查网络";
        } else if (e instanceof NullPointerException) {
            msg = "空指针异常";
        } else if (e instanceof javax.net.ssl.SSLHandshakeException) {
            msg = "证书验证失败";
        } else if (e instanceof ClassCastException) {
            msg = "类型转换错误";
        } else if (/*e instanceof JsonParseException ||*/
            e instanceof JSONException ||
                e instanceof com.alibaba.fastjson.JSONException ||
                /*e instanceof JsonSerializer ||*/
                e instanceof NotSerializableException ||
                e instanceof ParseException) {
            msg = "解析错误";
        }
        return msg;
    }
}
