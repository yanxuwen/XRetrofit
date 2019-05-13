# 前言
极简HTTP请求，基于OkHttp,仿retrofit的注解方式，适用于Android跟java的使用。如果是测试感兴趣的话，我会在提供一个连接教你们如何导入，后续会提供

#### 只需要简单的2步骤就能实现请求。
#### 定义接口
~~~
@NetServiceClass("")
public interface NetService {
    /**
     * 表单提交
     */
    @POST("https://marathonbeta.321go.com/api/v5/assis/user")
    void postForm(@Field("token") String token, @Field("auid") String auid, @Field("step") String step, @Field("formId") String formId, DataCallBack callBack);

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
        //String.class代表返回类型，可以任何类型，记得要跟json的格式进行匹配。
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
好了就这么简单，接口的定义清晰明了。已经看懂的人可以直接看demo
***

## 下面讲解下，如何使用。
##### 1、添加依赖
~~~
 implementation 'com.yanxuwen:http-api:1.0.5'
 annotationProcessor 'com.yanxuwen:http-compiler:1.0.5'
~~~
##### 2、定义接口
 如一张图，创建一个接口类，下面给一个完整接口定义，包含（get提交，表单提交，json提交，还有统一接口处理）
~~~
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

    void setHttpDealMethod(HttpDealMethod l);
~~~
##### 3、初始化NetService 类
~~~
public class HttpRequest {

    private static NetService netService;

    public static NetService getNetService() {
        try {
            if (netService == null) {
                synchronized (HttpRequest.class) {
                    if (netService == null) {
                        netService = (NetService) Class.forName(ElementUtils.getImplName(NetService.class))
                                .getConstructor().newInstance();
                        //需要接口统一处理，在打开
//                        netService.setHttpDealMethod(new HttpDealMethodImpl());
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
##### 4、然后就是用上面的的类直接获取NetService 来调用请求，HttpRequest.getNetService().postForm()
***
##### 5、注解的使用，我们按照第2点的定义接口的图片按顺序一个个解释，
###### NetServiceClass
@NetServiceClass("") ： 创建接口类的时候，这句话是必备的，括号里面可以填写你们公司的域名，如：
@NetServiceClass("http://a.szy.com:4480/")
$\color{red}{注意如果设置了通用的域名，下面的get跟post的url就可以不需要填写域名了}$
###### Query
用于retrofit就了解他的用法，就是拼接在url后面的参数，如上图的接口，使用Query后，url变成：
https://qybeta.321go.com/api/v1/home/index?cid=xxxxxx&token=xxxxx
###### Path
也是跟retrofit 一样，就说我们url的中间有些值是动态的，如图的urlhttps://qybeta.321go.com/api/v1/home/index中的v1是动态的，写法就是把v1写成{version}，然后在参数@Path("version") String version 就可以动态替换了
###### Field
看注释我们写着表单提交，那就是用于提交表单的字段
###### Param
这个retrofit 是没有的，看注释是用于json提交，但是只限制一层的json,
$\color{red}{他还有一个很好用的用法，就是统一接口处理，不限制是表单提交还是json提交，但是需要配置下。后面会讲解如何使用}$
###### Body 
@Body String json 就是用于整串的json格式
@Body LoginBuild json 就是json字符串转化成实体类进行，这个可能会很经常使用

###### Deal，

~~~
 /**
     * 请求跟返回经过统一特殊处理。
     */
    @POST("http://a.szy.com:4480/SignManageServer/sign/appHandle")
    @Deal
    void onDeal(@Field("reqcode")String reqcode,@Param("pageNo") String pageNo, @Param("pageSize") String pageSize, @Param("schoolId") String schoolId, DataCallBack callBack);

~~~
我们看到最后一个接口，我们有用到@Deal ，@Field ，@Param，是不是很混乱，
我们先来看下我们公司的请求：
![](https://upload-images.jianshu.io/upload_images/6835615-df1ea78c695aac0a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![](https://upload-images.jianshu.io/upload_images/6835615-968319098373cf6c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
我们每个接口都要自带一个Cookies,然后提交是表单提交，然后有固定的值，又有变化的值，然后每个接口都需要，那我岂不是每次写接口的时候，每次都要写这些参数，是不是很复杂，，最关键的body里面的值竟然是json传。这种接口不封装下岂能忍。
我们看到第2点的定义接口图，最后一行代码，如果不需统一处理，这句话可以不用写，方法名字也不需要按照图片的来，只要参数HttpDealMethod 有这个就可以
~~~
void setHttpDealMethod(HttpDealMethod l);
~~~
然后第3点的图片有一个注释代码，那句话打开，就是设置统一处理类，
然后在我们的定义接口的方法，打上@Deal这样的标记，就是代表该接口请求的时候要处理下，如果你需要每个接口都需要处理，那就在类上面打上@DealClass  代表该类下，所以的接口请求，都需要统一处理。
如：
~~~
 @POST("http://a.szy.com:4480/SignManageServer/sign/appHandle")
 @Deal
 void onDeal(@Field("reqcode")String reqcode,@Param("pageNo") String pageNo, @Param("pageSize") String pageSize, @Param("schoolId") String schoolId, DataCallBack callBack);
~~~
~~~
@NetServiceClass("")
@DealClass
public interface NetService {
~~~
然后我们看下HttpDealMethodImpl类，就实现统一处理的类。
~~~
**
 * 统一处理方法跟回调
 */
public class HttpDealMethodImpl implements HttpDealMethod {
    @Override
    public void init(OkHttpClient okHttpClient) {

    }

    /**
     * 处理请求
     * 如果处理后，各种字段都有，则会优先表单请求，然后再者json请求
     * 注意一定要return dealParams 不然不处理
     */
    @Override
    public DealParams dealRequest(DealParams dealParams) {
        //设置Cookie
        Map<String, String> headers = dealParams.getHeaders();
        headers.put("Cookie","JSESSIONID=AE7B1C9D73D448EEAECF5EC8363C55B0"
                + ";ClientVersion=6.8.1");
        dealParams.setHeaders(headers);
        //设置表单参数
        Map<String, String> mapField = dealParams.getMapField();
        mapField.put("reqcodeversion","6.8");
        //获取@Params里的参数，然后设置成json串，设置到表单body里
        Map<String, String> mapParams = dealParams.getParams();
        JSONObject jb = new JSONObject();
        String json = "";
        for (Map.Entry<String, String> entry : mapParams.entrySet()) {
            try {
                jb.put(entry.getKey(), entry.getValue());
            } catch (JSONException e) {
            }
        }
        json = jb.toString();
        mapField.put("body",json);

        dealParams.setMapField(mapField);
        return dealParams;
    }

    /**
     * 处理回调,
     * 如果要设置返回错误，则new CallBack(-1,"请求失败") ，第一个参数不能为0即可，0代表成功
     * 如果要请求成功，直接 new CallBack(json)
     * return null 则不做任何处理
     */
    @Override
    public CallBack dealCallBack(String str) {
        String json = null;
        try {
            JSONObject jsonObject = JSON.parseObject(str);
            json = jsonObject.getString("body");
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        return  new CallBack(-1,"请求失败");
        return new CallBack(json);
    }
}

~~~
我们看到两个实现方法，分别是dealRequest，跟dealCallBack ，处理请求跟处理回调。
我们再去看下我们公司请求图片，需要每个接口添加Cookie
### 然后我们就在dealRequest里，看到
~~~
 //设置Cookie
        Map<String, String> headers = dealParams.getHeaders();
        headers.put("Cookie","JSESSIONID=AE7B1C9D73D448EEAECF5EC8363C55B0"
                + ";ClientVersion=6.8.1");
        dealParams.setHeaders(headers);
~~~
这样我们就可以为我们需要的接口添加Cookie，然后我们公司接口请求再去看下参数，reqcodeversion是固定，然后直接
~~~
   //设置表单参数
        Map<String, String> mapField = dealParams.getMapField();
        mapField.put("reqcodeversion","6.8");
~~~
body里面就有的复杂了，我们再来看下是怎么定义接口，跟请求的图片
~~~
/**
     * 请求跟返回经过统一特殊处理。
     */
    @POST("http://a.szy.com:4480/SignManageServer/sign/appHandle")
    @Deal
    void onDeal(@Field("reqcode")String reqcode,@Param("pageNo") String pageNo, @Param("pageSize") String pageSize, @Param("schoolId") String schoolId, DataCallBack callBack);
~~~
![image.png](https://upload-images.jianshu.io/upload_images/6835615-7ae195b82b0edfbb.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

我们看到参数里面有2种注解

@Field 跟,@Param
因为我们接口是表单，reqcode是动态变化的
所以@Field("reqcode")String reqcode,只要好理解，然后body里面的是一个json串，
这时候我们就可以使用@Param，这时候的@Param不再是上面解释的那样用于json提交。而是自定义参数，只要你们公司有规律性，那么就可以用@Param，因为你已经设置了统一处理，所以@Param变身为，任意形态的参数。

我们可以看到dealRequest里面dealParams.getParams();获取@Param里的所以字段值，然后循环取出里面的字段，转换成json串，然后添加到body的里面，然后在dealParams.setMapField(mapField);设置表单的值，，这样我们请求不必要写的就不需要写了，，简单的传递几个参数就可以，，具体的逻辑按你们公司的来，这里只是简单的举个我们公司的例子。

### dealCallBack
然后还要一个dealCallBack用于处理回调，比如我们可以在这里面统一处理下服务器返回的字段下是正确还是错误，然后取出你指定下的字段值，比如我们公司的，我只取出body里面的参数进行返回，其他参数不需要。
#### init用于初始化，目前功能暂未开放，下次版本再弄，就是初始化一些参数，比如超时时间之类的设置


***
#### 哦对了还一个注解忘记讲了，那就是头部，用法就
~~~
    @GET("https://qybeta.321go.com/api/v1/home/index")
    @Headers({"Content-Type:application/json; charset=utf-8"
             ,"Content-Type:application/json; charset=utf-8"})
    void get(@Query("cid") String cid, @Query("token") String token, DataCallBack callBack);
~~~
如果是动态的话那就是，弄在参数里面
~~~
@GET("https://qybeta.321go.com/api/v1/home/index")
    void get(@Header("Content-Type")String headType, @Query("cid") String cid, @Query("token") String token, DataCallBack callBack);
~~~
***
如果不需要异步，需要同步的话，那就是吧void改成返回类型，当然不只限定String类型，会根据你写的类型，进行将json转换，去掉参数DataCallBack callBack
如：
~~~
    @GET("https://qybeta.321go.com/api/v1/home/index")
    String get(@Query("cid") String cid, @Query("token") String token);
~~~
***
# 本章到此结束，还是不太懂的话，看下代码，代码没几行，看一下就懂了.
### github  [点击跳转](https://github.com/yanxuwen/okhttp)
### 如果你喜欢就去 github 帮我star下,非常感谢o(∩_∩)o~~~






