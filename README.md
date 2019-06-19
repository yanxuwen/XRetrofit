
##### 1、添加依赖
~~~
    implementation 'com.yanxuwen:http-api:1.1.7'
    annotationProcessor 'com.yanxuwen:http-compiler:1.1.7'
~~~
#### 只需要简单的2步骤就能实现请求。

#### 定义接口
~~~

@DealClass(HttpDealMethodImpl.class)
//@DealAll
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
    void onDeal(@Field("reqcode") String reqcode, @Param("pageNo") String pageNo, @Param("pageSize") String pageSize, @Param("schoolId") String schoolId, DataCallBack callBack);


}
~~~
#### 执行请求
~~~
/**
     * 表单提交
     */
    public void postForm(View view) {
        String token = "QpglbXabpgvKa5d1cqjq5Qb6KKldbvz6dmr0AVjXUlljQsVC5gkKA8IkEChX1ssY";
        String auid = "2c93e148674de85b01674ebc7e760018";
        String step = "2";
        String formId = "4f0e43a7c6095adfc0e5b216d9914f9e";
      //返回类型支持java实体类，只需要在方法返回类型String换成java实体类，它会自动给你转化成实体类。
        HttpRequest.getNetService().postForm(token, auid, step, formId, new DataCallBack<String>(String.class) {
            @Override
            public void onHttpSuccess(String result) {
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
            }
            @Override
            public void onHttpFail(NetError netError) {
                Toast.makeText(MainActivity.this, netError.getMessage(), Toast.LENGTH_LONG).show();
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
### demo [点击下载](https://pan.baidu.com/s/17B5EcF2_b__LCR-wfX8A0A)
### 提取码：4p2y
### 完整版简书 [点击跳转](https://www.jianshu.com/p/96ef31f6c56c)
### 如果你喜欢就去 github 帮我star下,非常感谢o(∩_∩)o~~~
