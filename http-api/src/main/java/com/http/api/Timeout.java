package com.http.api;

public class Timeout {

    private long connectTimeout ;
    private long readTimeout;
    private long writeTimeout;

    public Timeout(){
        this.connectTimeout = 10 * 1000;
        this.readTimeout = 10 * 1000;
        this.writeTimeout = 10 * 1000;
    }

    public Timeout(long connectTimeout,long readTimeout,long writeTimeout){
       this.connectTimeout = connectTimeout;
       this.readTimeout = readTimeout;
       this.writeTimeout = writeTimeout;
    }

    public long getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(long connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public long getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(long readTimeout) {
        this.readTimeout = readTimeout;
    }

    public long getWriteTimeout() {
        return writeTimeout;
    }

    public void setWriteTimeout(long writeTimeout) {
        this.writeTimeout = writeTimeout;
    }
}
