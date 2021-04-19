#### 介绍
XRetrofit   是一个极致模仿Retrofit 的风格代码，代码没有Retrofit 复杂，简单易懂。
####
 支持`get`、`post`、`put`、`delete` 请求，这些使用都一样。
简化文件上传，跟下载 使用`@DOWNLOAD` 跟`@UPLOAD`
简化` post` 表单请求跟JOSN请求。
支持转换器，如果需要Gson的转换器，可以去demo那边去复制
支持适配器，内置跟Retrofit的`Call<String>`，当然了String可以替换任意的类型，跟Retrofit一样
没有内置`Rxjava的适配器`，如果需要，自己去demo那边复制。
##### 区别
`Retrofit`      属于运行时注解,通过动态代理生成一个代码
`XRetrofit`   属于编译时注解，通过APT生成代码，如果要学习APT，可以操作这篇文章[《带你了解APT》点击传送门](https://www.jianshu.com/p/00dce41e5d00)

##### 依赖,  只支持androidX,没有supper版本
~~~
    implementation 'com.yanxuwen.xretrofit:xretrofit:2.0.0'
    annotationProcessor 'com.yanxuwen.xretrofit:xretrofit-compiler:2.0.0'
~~~
##### 依赖,  由于jcenter要跑路了，所以已迁移到jitpack  
~~~
    implementation 'com.github.yanxuwen:xretrofit:0.0.8'
    annotationProcessor 'com.github.yanxuwen.xretrofit:compiler:0.0.8'
~~~
#### 使用方法，跟Retrofit一样，要定义接口，唯一区别在于在类上面要添加注解`@NetServiceClass`
#### 定义接口，该例子分别写了`Rxjava`  、`Call`  、`同步请求`的使用
~~~
@NetServiceClass
public interface NetService {
    /**
     * Rxjava  适配器
     */
    @GET("api/manage-home/v5/home/detail-v2")
    Observable<HomeInfoV5> get(@Query("page") int page, @Query("limit") int limit);

    /**
     * 自带Call  适配器
     */
    @GET("api/manage-home/v5/home/{version}")
    Call<HomeInfoV5> get(@Path("version") String version, @Query("page") int page, @Query("limit") int limit);

    /**
     * 同步请求
     */
    @GET("api/manage-home/v5/home/detail-v2")
    String get3(@Query("page") int page, @Query("limit") int limit);

}
~~~
#### 执行请求，以Call 适配器为例子
~~~
   public void onGet2(View view) {
        Call<HomeInfoV5> call = HttpRequest.getNetService().get("detail-v2", 0, 10);
        call.enqueue(new MyCallBack<HomeInfoV5>(this) {

            @Override
            public void success(HomeInfoV5 s) {
                Log.e("yxw", "onGet:" + s.getMsg());
            }

            @Override
            public void fail(String msg) {
                Log.e("yxw", "onGet:" + msg);
            }

            @Override
            public void cancel() {
                Log.e("yxw", "cancel");
            }
        });

    }
~~~
#### 执行请求，以Rxjava 适配器为例子
~~~

    /**
     * get请求
     */
    public void onGet(View view) {
        Observable<HomeInfoV5> observable = HttpRequest.getNetService().get(0, 10);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.e("yxw","取消订阅2");
                    }
                })
                .as(AutoDispose.<HomeInfoV5>autoDisposable(
                        AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_PAUSE)))//OnDestory时自动解绑
                .subscribe(new Observer<HomeInfoV5>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.e("yxw", "onGet2 onSubscribe ");
                    }

                    @Override
                    public void onNext(@NonNull HomeInfoV5 homeInfoV5) {
                        Log.e("yxw", "onGet2 onNext " + homeInfoV5.getMsg());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("yxw", "onGet2 fail " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.e("yxw", "onGet2 onComplete ");
                    }
                });

    }

    }
~~~
#### POST请求如何区分表单跟JOSN，，这里我们抛弃了Retrofit的复杂度
例子如下：表单请求
~~~
    @FORM
    @POST("https://bx-uat.bisinuolan.cn/api/login/mobile")
    Call<String> postForm(@Param("mobile") String mobile, @Param("sms_code") String sms_code);
~~~
注意2个注解 `@FORM` 跟`@Param`   我们舍弃了Retrofit的 `@Field` 跟`@FormUrlEncoded`  
 `@FORM` 替换了`@FormUrlEncoded`  来区分是否表单
 `@Param` 替换了`@Field`    ，
 `@Param` 有个好处就是：
1、不管是表单请求还JSON请求，都是可以作为参数来传参
2、如果用来Json请求的话，他会作为json格式的最外层数据，
哎这个怎么解释呢。就是如果你用@Param("userId")，那么最终传参是
~~~
{
	"userId": "c053e1a2-7335-4451-9201-e25e805a19f5-1578390821008"
}
~~~

 `@Body`    注解，可以这样传
~~~
    /**
     * json 整串提交
     */
    @POST("api/customer/v1/open/user/level/my")
    Call<String> postJson2(@Body String json);

    /**
     * json 实体类提交
     */
    @POST("api/customer/v1/open/user/level/my")
    Call<String> postJson(@Body LoginBuild json);
~~~
 `@Body`  注解跟Retrofit 注解一样，拥有一个功能，那是`转换器`，在添加转换器的时候，我们会通过 `@Body` 注解，判断当前是什么类型，然后通过类型，进行转换你想要的数据。。
上面的那个实体类提交就是这个原理，判断是Object类型的话，然后将Object类型转换为JSON格式进行提交。
那么你就可以定义你们公司需要啥类型，做啥处理。反正你们自己想。

***

#### 配置。这个是跟Retrofit是一样的。。

        OkHttpClient client = new OkHttpClient
                .Builder()
                .connectTimeout(15, TimeUnit.SECONDS)//连接超时时间
                .readTimeout(15, TimeUnit.SECONDS)//读取超时时间
                .writeTimeout(15, TimeUnit.SECONDS)//写入超时时间
                .retryOnConnectionFailure(false)//连接不上是否重连,false不重连
                .hostnameVerifier(new TrustAllHostnameVerifier())//校验名称,这个对象就是信任所有的主机,也就是信任所有https的请求
                .sslSocketFactory(SslUtils.getSslSocketFactory().sSLSocketFactory, SslUtils.getSslSocketFactory().trustManager)
                .build();
        HttpManager.Builder()
                .baseUrl("https://bxapi.bisinuolan.cn/")
                .addConverterFactory(GsonConverterFactories.create())//FastJson转换器、可以替换成Gson
                .addCallAdapterFactory(Rxjava2CallAdapterFactories.create())//Rxjava2适配器，不配置有自带Call适配器
                .client(client)
                .build();

#### 出如何初始呢，NetService （类名是随便定义的）接口呢，那就是下面那个
~~~
public class HttpRequest {

    private static NetService netService;

    public static NetService getNetService() {
        try {
            if (netService == null) {
                synchronized (HttpRequest.class) {
                    if (netService == null) {
                        netService = (NetService) Class.forName(HttpManager.getImplName(NetService.class))
                                .getConstructor().newInstance();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return netService;
    }
}

~~~

然后使用的时候就是直接
HttpRequest.getNetService().get(0, 10);
***
### github  [点击跳转](https://github.com/yanxuwen/XRetrofit)
### 如果你喜欢就去 github 帮我star下,非常感谢o(∩_∩)o~~~






