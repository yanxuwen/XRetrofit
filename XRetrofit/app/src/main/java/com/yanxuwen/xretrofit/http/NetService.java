package com.yanxuwen.xretrofit.http;

import com.xretrofit.call.Call;
import com.xretrofit.call.ProgressCall;
import com.yanxuwen.xretrofit_annotations.annotation.method.DELETE;
import com.yanxuwen.xretrofit_annotations.annotation.method.DOWNLOAD;
import com.yanxuwen.xretrofit_annotations.annotation.method.FORM;
import com.yanxuwen.xretrofit_annotations.annotation.method.GET;
import com.yanxuwen.xretrofit_annotations.annotation.method.POST;
import com.yanxuwen.xretrofit_annotations.annotation.method.PUT;
import com.yanxuwen.xretrofit_annotations.annotation.method.UPLOAD;
import com.yanxuwen.xretrofit_annotations.annotation.param.Body;
import com.yanxuwen.xretrofit_annotations.annotation.param.FilePath;
import com.yanxuwen.xretrofit_annotations.annotation.param.Header;
import com.yanxuwen.xretrofit_annotations.annotation.param.Param;
import com.yanxuwen.xretrofit_annotations.annotation.param.Path;
import com.yanxuwen.xretrofit_annotations.annotation.param.Query;
import com.yanxuwen.xretrofit_annotations.annotation.service.NetServiceClass;
import com.yanxuwen.xretrofit.bean.HomeInfoV5;
import com.yanxuwen.xretrofit.bean.LoginBuild;

import io.reactivex.Observable;

@NetServiceClass
public interface NetService {
    /**
     * get
     */
    @GET("api/manage-home/v5/home/detail-v2")
    Observable<HomeInfoV5> get(@Query("page") int page, @Query("limit") int limit);

    /**
     * get请求(URL中带有参数)
     */
    @GET("api/manage-home/v5/home/{version}")
    Call<HomeInfoV5> get(@Path("version") String version, @Query("page") int page, @Query("limit") int limit);

    /**
     * 表单提交
     */
    @FORM
    @POST("https://bx-uat.bisinuolan.cn/api/login/mobile")
    Call<String> postForm(@Param("mobile") String mobile, @Param("sms_code") String sms_code);

    /**
     * json提交
     */
    @POST("https://bx-co-uat-appgateway.bsnlco.com/api/customer/v1/open/user/level/my")
    Call<String> postJson(@Param("userId") String userId);

    /**
     * json 整串提交
     */
    @POST("https://bx-co-uat-appgateway.bsnlco.com/api/customer/v1/open/user/level/my")
    Call<String> postJson2(@Body String json);

    /**
     * json 实体类提交
     */
    @POST("https://bx-co-uat-appgateway.bsnlco.com/api/customer/v1/open/user/level/my")
    Call<String> postJson(@Body LoginBuild json);

    /**
     * put 提交
     */
    @PUT("http://api.sdwhcn.com:5056/v1/member")
    Call<String> put(@Header("Authorization") String header, @Query("nickname") String nickname, @Query("signature") String signature, @Query("area") String area);

    /**
     * delete 提交
     */
    @DELETE("http://api.sdwhcn.com:5056/v1/member_collect_article/{id}")
    Call<String> delete(@Header("Authorization") String header, @Path("id") String id);

    /**
     *
     */
    @DOWNLOAD("https://ztjyupdate.ztjy61.com/333897c77ec9a86605006679c7a4b418-ZTJY")
    ProgressCall<String> download(@FilePath String file);

    /**
     * 多图上传
     */
    @UPLOAD("https://bxuatapi.bisinuolan.cn/api/bsnl-oss/appUploadShot")
    ProgressCall<String> upload(@FilePath String file, @Param("shot") boolean shot);

}