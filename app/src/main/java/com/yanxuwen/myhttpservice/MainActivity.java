package com.yanxuwen.myhttpservice;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.http.api.DataCallBack;
import com.http.api.NetError;
import com.http.api.ProgressCallBack;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.yanxuwen.myhttpservice.bean.LoginBuild;
import com.yanxuwen.myhttpservice.http.HttpRequest;

import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * get请求
     */
    public void onGet(View view) {
        HttpRequest.getNetService().get("8a9a488566624d8301667556664e0001", "QpglbXabpgvKa5d1cqjq5Qb6KKldbvz6dmr0AVjXUlljQsVC5gkKA8IkEChX1ssY", new DataCallBack<String>(String.class) {

            @Override
            public void onHttpSuccess(String result) {
                Log.e("yxw","onGet :" + result);
            }

            @Override
            public void onHttpFail(NetError netError) {
                Log.e("yxw","onGet fail :" + netError.getMessage());
            }
        });
    }

    /**
     * get请求(URL中带有参数,也支持post)
     */
    public void onGet2(View view) {
        HttpRequest.getNetService().get("v1", "8a9a488566624d8301667556664e0001", "QpglbXabpgvKa5d1cqjq5Qb6KKldbvz6dmr0AVjXUlljQsVC5gkKA8IkEChX1ssY", new DataCallBack<String>(String.class) {
            @Override
            public void onHttpSuccess(String result) {
                Log.e("yxw","onGet2 :" + result);
            }

            @Override
            public void onHttpFail(NetError netError) {
                Log.e("yxw","onGet2 fail :" + netError.getMessage());
            }
        });
    }

    /**
     * 表单提交
     */
    public void postForm(View view) {
        String token = "QpglbXabpgvKa5d1cqjq5Qb6KKldbvz6dmr0AVjXUlljQsVC5gkKA8IkEChX1ssY";
        String auid = "2c93e148674de85b01674ebc7e760018";
        String step = "2";
        String formId = "4f0e43a7c6095adfc0e5b216d9914f9e";
        HttpRequest.getNetService().postForm(token, auid, step, formId, new DataCallBack<String>(String.class) {
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

    /**
     * json提交
     */
    public void postJson(View view) {
        String mobile = "15060568265";
        String password = "e10adc3949ba59abbe56e057f20f883e";
        HttpRequest.getNetService().postJson(mobile, password, new DataCallBack<String>(String.class) {
            @Override
            public void onHttpSuccess(String result) {
                Log.e("yxw","postJson :" + result);
            }

            @Override
            public void onHttpFail(NetError netError) {
                Log.e("yxw","postJson fail :" + netError.getMessage());
            }
        });
    }

    /**
     * json 整串提交
     */
    public void postJson2(View view) {
        String json = "{\r\n" +
                "	\"password\": \"e10adc3949ba59abbe56e057f20f883e\",\r\n" +
                "	\"mobile\": \"15060568265\"\r\n" +
                "}";
        HttpRequest.getNetService().postJson(json, new DataCallBack<String>(String.class) {
            @Override
            public void onHttpSuccess(String result) {
                Log.e("yxw","postJson :" + result);
            }

            @Override
            public void onHttpFail(NetError netError) {
                Log.e("yxw","postJson fail :" + netError.getMessage());
            }
        });
    }

    /**
     * json 整串提交
     */
    public void postJson3(View view) {
        LoginBuild mLoginBuild = new LoginBuild();
        mLoginBuild.setMobile("15060568265");
        mLoginBuild.setPassword("e10adc3949ba59abbe56e057f20f883e");
        HttpRequest.getNetService().postJson(mLoginBuild, new DataCallBack<String>(String.class) {
            @Override
            public void onHttpSuccess(String result) {
                Log.e("yxw","postJson3 :" + result);
            }

            @Override
            public void onHttpFail(NetError netError) {
                Log.e("yxw","postJson3 fail :" + netError.getMessage());
            }
        });
    }

    /**
     * put 提交
     */
    public void onPut(View view) {
      HttpRequest.getNetService().put("header","测试","signature","area", new DataCallBack<String>(String.class) {
          @Override
            public void onHttpSuccess(String result) {
              Log.e("yxw","onPut :" + result);
            }

            @Override
            public void onHttpFail(NetError netError) {
                Log.e("yxw","onPut fail :" + netError.getMessage());
            }
        });
    }

    /**
     * delete 提交
     */
    public void onDelete(View view) {
       HttpRequest.getNetService().delete("header","123231" ,new DataCallBack<String>(String.class) {
            @Override
            public void onHttpSuccess(String result) {
                Log.e("yxw","onDelete :" + result);
            }

            @Override
            public void onHttpFail(NetError netError) {
                Log.e("yxw","onDelete fail :" + netError.getMessage());
            }
        });
    }

    public void onDownload(View v){
        //先权限申请，在请求
        RxPermissions.getInstance(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        if (granted) {
                            String path = Environment.getExternalStorageDirectory().toString() + "/测试";//获取目录
                            HttpRequest.getNetService().download(path,"text.apk", new ProgressCallBack() {
                                @Override
                                public void onHttpSuccess(Object result) {
                                    Log.e("yxw","onDownload :" + result);
                                }

                                @Override
                                public void onHttpFail(NetError netError) {
                                    Log.e("yxw","onDownload fail :" + netError.getMessage());
                                }

                                @Override
                                public void onLoadProgress(float progress) {
                                    Log.e("yxw","onDownload progress:" + progress);
                                }
                            });
                        } else {
                            Toast.makeText(MainActivity.this,"请打开存储权限",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void onUpload(View v){
        //先权限申请，在请求
        RxPermissions.getInstance(this).request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        if (granted) {
                            //允许权限
                            String path = Environment.getExternalStorageDirectory().toString() + "/huawei/MagazineUnlock/magazine-unlock-01-2.3.1344-_132FEBAC9815C7732FE627DD6380E5CA.jpg";//获取跟目录
                            String header = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjFmMDRiYjYzMjEyYzcwNzE3NTI0YzQ4MDJhNjg2NTU0OTQ1NTU0YjI2ZjNmZmZlOGIxN2EzNGFmOGVjNTJlMzVkNWQxNGNmOWFiOTVhZTc3In0.eyJhdWQiOiIyIiwianRpIjoiMWYwNGJiNjMyMTJjNzA3MTc1MjRjNDgwMmE2ODY1NTQ5NDU1NTRiMjZmM2ZmZmU4YjE3YTM0YWY4ZWM1MmUzNWQ1ZDE0Y2Y5YWI5NWFlNzciLCJpYXQiOjE1NjEzNDE0OTgsIm5iZiI6MTU2MTM0MTQ5OCwiZXhwIjoxNTYxNDI3ODk4LCJzdWIiOiIxIiwic2NvcGVzIjpbIioiXX0.MFjJOXjQ303UY6h-nXMF0Q4-5pZqcKLbY3OYR2dM0FCNm46KbYNQf0dzLWikZbu7-cguJSHzPk4FfJogus6IAThcz2An_5wjb8y5xjVZGH-Nl1B7P1S6HE8dHX_ulX53nZdyRY-2PaQLxlclihUkesQISY5ul7MFw4TvxNp_hbjt03zDLzHhY93UDYFSvIafUch44BARwNWmuAJ7fk5DmpEGfjaTxkU9p_A9ii1sLDZ8kzSq_QmtgNKBEDkUQXgvrdAjABzcYkcHgbCeZLevN91xPc8EoJi0VsyuLRuncn-1Xz3ifp0ASAQtyYrJiy91NenV_19t5dR1Ltp6sHgwa1_g45kbCKQTJZ44skwtVABgOCw4IQLvzczQGpSRli20qpADU76ADZmnaKmLiCHGQsVWFJYy9_FS4SVW4yrWnztV91FSyg0DyqmH4jatjOl5PnnUMQfqE-N15a5VVhZem3cszkH2OLj34C2KMuR0tbWnWWyFVdhTZ0MN63BGKC0NXQOQ6sFu1gEjwnrRwkz1Yt8GTpZ8fct1CjhTQjd-7brVBW8rhumlvgsw_OE6eszVnransAf4VxMCN5xTDpyZWcKcTyL9PtzRT8d65V88y6iBPHeVVoEXMVUZe4W1DJYjG1bbzG32rLsLdemBNZTEKG2RKrlpHwnrobDAJcFl1x4";
                            HttpRequest.getNetService().upload(header,new String[]{path,path},new String[]{"file","file"},new String[]{"test.jpg","test2.jpg"}, new ProgressCallBack() {
                                @Override
                                public void onHttpSuccess(Object result) {
                                    Log.e("yxw","onUpload :" + result);
                                }

                                @Override
                                public void onHttpFail(NetError netError) {
                                    Log.e("yxw","onUpload fail :" + netError.getMessage());
                                }

                                @Override
                                public void onLoadProgress(float progress) {
                                    Log.e("yxw","onUpload progress:" + progress);
                                }
                            });
                        } else {
                            Toast.makeText(MainActivity.this,"请打开存储权限",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void onUpload2(View v){
        //先权限申请，在请求
        RxPermissions.getInstance(this).request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        if (granted) {
                            //允许权限
                            String path = Environment.getExternalStorageDirectory().toString() + "/huawei/MagazineUnlock/magazine-unlock-01-2.3.1344-_132FEBAC9815C7732FE627DD6380E5CA.jpg";//获取跟目录
                            String header = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjFmMDRiYjYzMjEyYzcwNzE3NTI0YzQ4MDJhNjg2NTU0OTQ1NTU0YjI2ZjNmZmZlOGIxN2EzNGFmOGVjNTJlMzVkNWQxNGNmOWFiOTVhZTc3In0.eyJhdWQiOiIyIiwianRpIjoiMWYwNGJiNjMyMTJjNzA3MTc1MjRjNDgwMmE2ODY1NTQ5NDU1NTRiMjZmM2ZmZmU4YjE3YTM0YWY4ZWM1MmUzNWQ1ZDE0Y2Y5YWI5NWFlNzciLCJpYXQiOjE1NjEzNDE0OTgsIm5iZiI6MTU2MTM0MTQ5OCwiZXhwIjoxNTYxNDI3ODk4LCJzdWIiOiIxIiwic2NvcGVzIjpbIioiXX0.MFjJOXjQ303UY6h-nXMF0Q4-5pZqcKLbY3OYR2dM0FCNm46KbYNQf0dzLWikZbu7-cguJSHzPk4FfJogus6IAThcz2An_5wjb8y5xjVZGH-Nl1B7P1S6HE8dHX_ulX53nZdyRY-2PaQLxlclihUkesQISY5ul7MFw4TvxNp_hbjt03zDLzHhY93UDYFSvIafUch44BARwNWmuAJ7fk5DmpEGfjaTxkU9p_A9ii1sLDZ8kzSq_QmtgNKBEDkUQXgvrdAjABzcYkcHgbCeZLevN91xPc8EoJi0VsyuLRuncn-1Xz3ifp0ASAQtyYrJiy91NenV_19t5dR1Ltp6sHgwa1_g45kbCKQTJZ44skwtVABgOCw4IQLvzczQGpSRli20qpADU76ADZmnaKmLiCHGQsVWFJYy9_FS4SVW4yrWnztV91FSyg0DyqmH4jatjOl5PnnUMQfqE-N15a5VVhZem3cszkH2OLj34C2KMuR0tbWnWWyFVdhTZ0MN63BGKC0NXQOQ6sFu1gEjwnrRwkz1Yt8GTpZ8fct1CjhTQjd-7brVBW8rhumlvgsw_OE6eszVnransAf4VxMCN5xTDpyZWcKcTyL9PtzRT8d65V88y6iBPHeVVoEXMVUZe4W1DJYjG1bbzG32rLsLdemBNZTEKG2RKrlpHwnrobDAJcFl1x4";
                            HttpRequest.getNetService().upload(header,path,"file", "test.jpg", new ProgressCallBack() {
                                @Override
                                public void onHttpSuccess(Object result) {
                                    Log.e("yxw","onUpload2 :" + result);
                                }

                                @Override
                                public void onHttpFail(NetError netError) {
                                    Log.e("yxw","onUpload2 fail :" + netError.getMessage());
                                }

                                @Override
                                public void onLoadProgress(float progress) {
                                    Log.e("yxw","onUpload2 progress:" + progress);
                                }
                            });
                        } else {
                            Toast.makeText(MainActivity.this,"请打开存储权限",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void onTest(View view) {
        String reqcode = "10960";
        String pageNo = "1";
        String pageSize = "20";
        String schoolId = "XWQMI77m9Fbv1GWkBba";
        //String.class代表返回类型，可以任何类型，记得要跟json的格式进行匹配。
        HttpRequest.getNetService().onDeal(reqcode,pageNo,pageSize,schoolId , new DataCallBack<String>(String.class) {
            @Override
            public void onHttpSuccess(String result) {
                Log.e("yxw","onTest :" + result);
            }

            @Override
            public void onHttpFail(NetError netError) {
                Log.e("yxw","onTest fail :" + netError.getMessage());
            }
        });
    }
}
