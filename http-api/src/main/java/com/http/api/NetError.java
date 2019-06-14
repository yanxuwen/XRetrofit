//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.http.api;

public class NetError {
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    public interface HttpErrorCode {
        int ERROR= -10001;
        int DATA_EMPTY = -10002;
        int NET_DISCONNECT = -10003;
        int DATA_ERROR = -10004;
        int NET_TIMEOUT = -10005;
    }
    private int httpCode;
    private int code;
    private String message;
    private String errorMessage;

    public NetError(int httpCode ,int code, String message,String errorMessage) {
        this.httpCode = httpCode;
        this.code = code;
        this.message = message;
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
