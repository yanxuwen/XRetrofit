//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.http.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yanxuwen.json.JsonUtils;

public class NetError {
    public String getMessage() {
        return message;
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
        /**文件没找到*/
        int FILE_NOT_FOUND= -10006;

    }
    private int httpCode;
    private int code;
    private String message;
    private String errorMessage;

    public NetError(int httpCode ,int code, String message,String errorMessage) {
        this.httpCode = httpCode;
        this.code = code;
        this.errorMessage = errorMessage;
        this.message = message;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String toString(){
        String json = JsonUtils.toString(this);
        String message = JsonUtils.parse(json,String.class,"message");
        if (message != null && !message.equals("") && JsonUtils.isJson(message)){
            try {
                JSONObject jsonObject = JSON.parseObject(json);
                JSONObject jmessage = JSON.parseObject(message);
                jsonObject.put("message",jmessage);
                return jsonObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return json;
    }
}
