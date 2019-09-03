#### 支持get、post、put、delete、文件下载断点续传、多图上传等功能。只需要定义方法即可调用接口，不需要任何逻辑。

##### 1、添加依赖
~~~
    implementation 'com.yanxuwen:http-api:1.2.3'
    annotationProcessor 'com.yanxuwen:http-compiler:1.2.3'
~~~
#### 只需要简单的2步骤就能实现请求。

#### 定义接口
~~~

//@DealAll 打上DealAll标志 所有的接口 请求跟返回经过统一特殊处理。特殊处理将在HttpDealMethodImpl类里面执行
@DealClass(HttpDealMethodImpl.class) //处理类
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
    @Deal //打上Deal标志 请求跟返回经过统一特殊处理。特殊处理将在HttpDealMethodImpl类里面执行
    @Retry(3)//重试次数
    @TimeOut(3000)//超时时间为3s
    void onDeal(@Field("reqcode") String reqcode, @Param("pageNo") String pageNo, @Param("pageSize") String pageSize, @Param("schoolId") String schoolId, DataCallBack callBack);

}
~~~
#### 执行请求
~~~
/**
     * 表单提交
     */
    public void postForm(View view) {
            HttpRequest.getNetService().postForm("10960",new DataCallBack<String>(String.class,this) {
            @Override
            public void onHttpSuccess(String result) {
                Log.e("yxw","postForm :" + result);
            }

            @Override
            public void onHttpFail(NetError netError) {
                Log.e("yxw","postForm fail :" + netError.getMessage());
            }
        });
    }
~~~
### 提供一个Json解析器
~~~
 我们可以看到返回类型支持java实体类跟String类型
但是有时候直接转换实体类有点麻烦，这里我们提供一个JsonUtils解析
直接
  int forum_id = JsonUtils.parse(json,Integer.class,"notice","forum_id");
我们可以看到直接解析出第二层forum_id里面的数据，直接转换成int类型，
如果需要转换实体类，直接在第二个参数改成   类名.class  ,然后指定那一层就解析那一层数据转化成实体类。
这样是不是很方便。
~~~
***
### demo [点击下载](https://pan.baidu.com/s/1iQZAA3sSKt3mMeyhGUuTvg)
### 提取码：7jif 
### 完整版简书 [点击跳转](https://www.jianshu.com/p/96ef31f6c56c)
### github  [点击跳转](https://github.com/yanxuwen/okhttp)
### 如果你喜欢就去 github 帮我star下,非常感谢o(∩_∩)o~~~
