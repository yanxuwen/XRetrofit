package com.http.compiler.bean;

public class CallBack {

    public CallBack(String msg){
        this.msg = msg;
    }

    public  CallBack(int returnCode ,String msg){
        this.returnCode = returnCode;
        this.msg = msg;
    }

    private int returnCode;
    private String msg;

    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
