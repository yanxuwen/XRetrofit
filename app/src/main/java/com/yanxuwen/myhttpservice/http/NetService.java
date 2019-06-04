package com.yanxuwen.myhttpservice.http;

import com.http.api.DataCallBack;
import com.http.compiler.annotation.Body;
import com.http.compiler.annotation.Deal;
import com.http.compiler.annotation.DealAll;
import com.http.compiler.annotation.DealClass;
import com.http.compiler.annotation.Field;
import com.http.compiler.annotation.GET;
import com.http.compiler.annotation.NetServiceClass;
import com.http.compiler.annotation.POST;
import com.http.compiler.annotation.Param;
import com.http.compiler.annotation.Path;
import com.http.compiler.annotation.Query;
import com.yanxuwen.myhttpservice.bean.LoginBuild;

//@DealAll
@DealClass(HttpDealMethodImpl.class)
@NetServiceClass("")
public interface NetService {
    /**
     * get的简单请求
     */
    @GET("https://qybeta.321go.com/api/v1/home/index")
    void get(@Query("cid") String cid, @Query("token") String token, DataCallBack callBack);

    /**
     * get请求(URL中带有参数)
     */
    @GET("https://qybeta.321go.com/api/{version}/home/index")
    void get(@Path("version") String version, @Query("cid") String cid, @Query("token") String token, DataCallBack callBack);

    /**
     * 表单提交
     */
    @POST("https://marathonbeta.321go.com/api/v5/assis/user")
    void postForm(@Field("token") String token, @Field("auid") String auid, @Field("step") String step, @Field("formId") String formId, DataCallBack callBack);

    /**
     * json提交
     */
    @POST("http://public.api.fashionworldcn.com/api/my/login")
    void postJson(@Param("mobile") String mobile, @Param("password") String password, DataCallBack callBack);

    /**
     * json 整串提交
     */
    @POST("http://public.api.fashionworldcn.com/api/my/login")
    void postJson(@Body String json, DataCallBack callBack);

    /**
     * json 实体类提交
     */
    @POST("http://public.api.fashionworldcn.com/api/my/login")
    void postJson(@Body LoginBuild json, DataCallBack callBack);

    /**
     * 请求跟返回经过统一特殊处理。
     */
    @POST("http://a.szy.com:4480/SignManageServer/sign/appHandle")
    @Deal
    void onDeal(@Field("reqcode")String reqcode,@Param("pageNo") String pageNo, @Param("pageSize") String pageSize, @Param("schoolId") String schoolId, DataCallBack callBack);

}
