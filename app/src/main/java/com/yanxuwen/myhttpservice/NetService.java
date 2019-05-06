package com.yanxuwen.myhttpservice;

import com.http.api.DataCallBack;
import com.http.api.HttpDealMethod;
import com.http.compiler.annotation.Body;
import com.http.compiler.annotation.DealClass;
import com.http.compiler.annotation.NetServiceClass;
import com.http.compiler.annotation.POST;

@NetServiceClass("")
@DealClass
public interface NetService {
//    @Headers({"Access-User-Token:e5cHLWScbto3VfvYTU1llVZgl/WniA4QZZ8epmn8k/o=",
//            "Access-User-Token2:e5cHLWScbto3VfvYTU1llVZgl/WniA4QZZ8epmn8k/o="})
//    @POST("http://api.sdwhcn.com:5056/v1/auth/login")
//    void post(@Header("xxxx") String xxxx, DataCallBack callBack);
//
//    @POST("http://api.sdwhcn.com:5056/v1/auth/login")
//    void post2(@Field("key") int json, DataCallBack callBack);
//
//    @POST("http://api.sdwhcn.com:5056/v1/auth/login")
//    void post3(@Param("param")String test, @Body String json, DataCallBack callBack);
//
//    @POST("http://api.sdwhcn.com:5056/v1/auth/login")
//    void post4(@Field("key")String name , @Query("tag")String tag , @Body String body, DataCallBack callBack);

    @POST("http://api.sdwhcn.com:5056/v1/auth/login")
    void post5(@Body Test body, DataCallBack callBack);

//    @POST("http://a.szy.com:4480/health/dose/register/list/p/v1.0")
//    void post6(@Body String body, DataCallBack callBack);

//    @POST("https://nuser.321go.com/sms/sendCode")
//    void post7(@Param("mobile") String mobile,DataCallBack callBack);

    public void setHttpDealMethod(HttpDealMethod httpDealMethod);
}
