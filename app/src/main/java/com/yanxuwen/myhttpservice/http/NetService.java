package com.yanxuwen.myhttpservice.http;

import com.http.DataCallBack;
import com.http.api.ProgressCallBack;
import com.http.compiler.annotation.method.Retry;
import com.http.compiler.annotation.method.TimeOut;
import com.http.compiler.annotation.service.RetryAll;
import com.http.compiler.annotation.param.Body;
import com.http.compiler.annotation.method.DELETE;
import com.http.compiler.annotation.method.DOWNLOAD;
import com.http.compiler.annotation.method.Deal;
import com.http.compiler.annotation.service.DealClass;
import com.http.compiler.annotation.param.Field;
import com.http.compiler.annotation.method.GET;
import com.http.compiler.annotation.param.Header;
import com.http.compiler.annotation.service.NetServiceClass;
import com.http.compiler.annotation.method.POST;
import com.http.compiler.annotation.method.PUT;
import com.http.compiler.annotation.param.Param;
import com.http.compiler.annotation.param.Path;
import com.http.compiler.annotation.param.Query;
import com.http.compiler.annotation.method.UPLOAD;
import com.yanxuwen.myhttpservice.bean.LoginBuild;

//@DealAll
@DealClass(HttpDealMethodImpl.class)
@NetServiceClass("")
public interface NetService {
    /**
     * get的简单请求
     */
    @GET("http://api.sdwhcn.com:5056/v1/temple")
    void get(@Query("page") int page, @Query("limit") int limit, @Query("recommend") String recommend, DataCallBack callBack);

    /**
     * get请求(URL中带有参数)
     */
    @GET("http://api.sdwhcn.com:5056/{version}/temple")
    void get(@Path("version") String version, @Query("page") int page, @Query("limit") int limit, @Query("recommend") String recommend, DataCallBack callBack);

    /**
     * 表单提交
     */
    @POST("http://a.szy.com:4480/SignManageServer/sign/appHandle")
    @Deal
    void postForm(@Field("reqcode") String reqcode, DataCallBack callBack);

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
    void put(@Header("Authorization") String header, @Query("nickname") String nickname, @Query("signature") String signature, @Query("area") String area, DataCallBack callBack);

    /**
     * delete 提交
     */
    @DELETE("http://api.sdwhcn.com:5056/v1/member_collect_article/{id}")
    void delete(@Header("Authorization") String header, @Path("id") String id, DataCallBack callBack);

    /**
     * 文件下载
     * 【注意】 文件下载的传参比较特殊，@Param 的key是按照服务端的字段来填写的，
     * 而文件下载不需要，所以@Param 的keykey是固定的，
     * filepath 代表文件路径，必填
     * filename 代表文件名称，如果下载没带后缀，可自行加上后缀。
     */
    @DOWNLOAD("https://ztjyupdate.ztjy61.com/333897c77ec9a86605006679c7a4b418-ZTJY")
    void download(@Param("filepath") String filepath, @Param("filename") String filename, ProgressCallBack callBack);

    /**
     * 多图上传
     * 【注意】 @Param 的key 跟文件下载一样是固定写法
     *
     * @param filepath 代表文件路径，必填
     * @param filekey  代表文件key，必填
     * @param filename 代表文件名称 选填
     * @param callBack
     */
    @UPLOAD("http://api.sdwhcn.com:5056/v1/member/avatar")
    void upload(@Header("Authorization") String header, @Param("filepath") String[] filepath, @Param("filekey") String[] filekey, @Param("filename") String[] filename, ProgressCallBack callBack);

    /**
     * 单张图片上传
     * 【注意】 @Param 的key 跟文件下载一样是固定写法
     *
     * @param filepath 代表文件路径，必填
     * @param filekey  代表文件key，必填
     * @param filename 代表文件名称 选填
     */
    @UPLOAD("http://api.sdwhcn.com:5056/v1/member/avatar")
    void upload(@Header("Authorization") String header, @Param("filepath") String filepath, @Param("filekey") String filekey, @Param("filename") String filename, ProgressCallBack callBack);

    /**
     * 请求跟返回经过统一特殊处理。
     */
    @POST("http://a.szy.com:4480/SignManageServer/sign/appHandle")
    @Deal
    @Retry(3)//重试次数
    @TimeOut(3000)//超时时间为3s
    void onDeal(@Field("reqcode") String reqcode, @Param("pageNo") String pageNo, @Param("pageSize") String pageSize, @Param("schoolId") String schoolId, DataCallBack callBack);

}
