package com.yanxuwen.myhttpservice.http;

import com.http.api.DataCallBack;
import com.http.api.ProgressCallBack;
import com.http.compiler.annotation.Body;
import com.http.compiler.annotation.DELETE;
import com.http.compiler.annotation.DOWNLOAD;
import com.http.compiler.annotation.Deal;
import com.http.compiler.annotation.DealAll;
import com.http.compiler.annotation.DealClass;
import com.http.compiler.annotation.Field;
import com.http.compiler.annotation.GET;
import com.http.compiler.annotation.Header;
import com.http.compiler.annotation.NetServiceClass;
import com.http.compiler.annotation.POST;
import com.http.compiler.annotation.PUT;
import com.http.compiler.annotation.Param;
import com.http.compiler.annotation.Path;
import com.http.compiler.annotation.Query;
import com.http.compiler.annotation.UPLOAD;
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
     * put 提交
     */
    @PUT("http://api.sdwhcn.com:5056/v1/member")
    void put(@Header("Authorization") String header, @Query("nickname") String nickname,@Query("signature") String signature,@Query("area") String area,DataCallBack callBack);

    /**
     * delete 提交
     */
    @DELETE("http://api.sdwhcn.com:5056/v1/member_collect_article/{id}")
    void delete(@Header("Authorization") String header, @Path("id") String id,DataCallBack callBack);

    /**
     * 文件下载
     * 【注意】 文件下载的传参比较特殊，@Param 的key是按照服务端的字段来填写的，
     * 而文件下载不需要，所以key是固定的，
     * filepath 代表文件路径，必填
     * filename 代表文件名称，如果下载没带后缀，可自行加上后缀。
     */
    @DOWNLOAD("https://ztjyupdate.ztjy61.com/333897c77ec9a86605006679c7a4b418-ZTJY")
    void download(@Param("filepath") String filepath, @Param("filename") String filename, ProgressCallBack callBack);

    /**
     * 多图上传
     */
    @UPLOAD("http://api.sdwhcn.com:5056/v1/member/avatar")
    void upload(@Header("Authorization") String header,@Param("filepath") String[] filepath, @Param("filekey") String[] filekey , @Param("filename") String[] filename, ProgressCallBack callBack);

    /**
     * 单张图片上传
     */
    @UPLOAD("http://api.sdwhcn.com:5056/v1/member/avatar")
    void upload(@Header("Authorization") String header,@Param("filepath") String filepath, @Param("filekey") String filekey , @Param("filename") String filename, ProgressCallBack callBack);

    /**
     * 请求跟返回经过统一特殊处理。
     */
    @POST("http://a.szy.com:4480/SignManageServer/sign/appHandle")
    @Deal
    void onDeal(@Field("reqcode")String reqcode,@Param("pageNo") String pageNo, @Param("pageSize") String pageSize, @Param("schoolId") String schoolId, DataCallBack callBack);

}
