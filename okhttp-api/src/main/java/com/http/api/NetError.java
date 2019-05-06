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

    public interface HttpErrorCode {
        int ERROR= 10001;
        int DATA_EMPTY = 3;
        int NET_DISCONNECT = 4;
        int DATA_ERROR = 5;
        int NET_TIMEOUT = 6;
    }
    private int code;
    private String message;
    private String errorMessage;

    public NetError(int code, String message,String errorMessage) {
        this.code = code;
        this.message = message;
        this.errorMessage = errorMessage;
    }

    public NetError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public NetError(int code) {
        this(code, "","");
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
