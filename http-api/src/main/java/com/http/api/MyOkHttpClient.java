//package com.http.api;
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.io.Serializable;
//
//import okhttp3.OkHttpClient;
//
//public class MyOkHttpClient extends OkHttpClient implements Serializable {
//    private static final long serialVersionUID = 2631590509760908280L;
//
//    private int connectTimeoutMillis = 1;
//    private int readTimeoutMillis = 1;
//    private int writeTimeoutMillis = 1;
//
//
//    public MyOkHttpClient(Builder builder){
//        super(builder);
//    }
//
//    public MyOkHttpClient deepClone() {
//        MyOkHttpClient cloneObj = null;
//        try {
//            //写入字节流
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            ObjectOutputStream obs = new ObjectOutputStream(out);
//            obs.writeObject(this);
//            obs.close();
//
//            //分配内存，写入原始对象，生成新对象
//            ByteArrayInputStream ios = new ByteArrayInputStream(out.toByteArray());
//            ObjectInputStream ois = new ObjectInputStream(ios);
//            //返回生成的新对象
//            cloneObj = (MyOkHttpClient) ois.readObject();
//            ois.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return cloneObj;
//    }
//}
