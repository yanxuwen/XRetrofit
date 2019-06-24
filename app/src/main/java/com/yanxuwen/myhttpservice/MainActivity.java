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
        HttpRequest.getNetService().get(0,10,"recommend", new DataCallBack<String>(String.class) {

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
        HttpRequest.getNetService().get("v1" , 0 ,10,"recommend", new DataCallBack<String>(String.class) {
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
        HttpRequest.getNetService().postForm("10960",new DataCallBack<String>(String.class) {
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
                            String header = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjY4MDFmZjE1NTdlNWJhMzkzY2RlM2U0NjRiMGM0MzQ3MDkwNWYzMDYwZTZhZDFkOTQ4NTIyY2I3NDI2YjY0ZDFmODBiOWJkODY0YmEyM2UxIn0.eyJhdWQiOiIyIiwianRpIjoiNjgwMWZmMTU1N2U1YmEzOTNjZGUzZTQ2NGIwYzQzNDcwOTA1ZjMwNjBlNmFkMWQ5NDg1MjJjYjc0MjZiNjRkMWY4MGI5YmQ4NjRiYTIzZTEiLCJpYXQiOjE1NjEzNjI2ODcsIm5iZiI6MTU2MTM2MjY4NywiZXhwIjoxNTYxNDQ5MDg3LCJzdWIiOiIxIiwic2NvcGVzIjpbIioiXX0.RXUUxeLvYkkk1V-pmu-120N5JejjaDmTfG0zO0Zu3lMc5OChjlSvDiKm2jW6geCIp2gZeOrkC4HBNpSngjKue_v1l1UyyYudOofTZV3DUlF-hwhhwMJ2RKxp6yq2ecGfxCcg3ZED1dp0dAjmqmNCGUZViykQctSQC7FI3KXQeL-96wQj6G9YnN0n2sVOkeH2m1AYR2YjkXFW3C-lMujiqbfoH0i_DyRWqmvnH4IS67L8Ec0dWBNgWbDWyrO6Za6z9Im6VHfeqVkVYbvFdKrN8mtNuQQ0oioG_6vvuLE9zV-p2YT1t_WogqieFJHb9C6t9QZCqDopU7QBKiczoSk72tMffL0j_Byn1TlG7TlN0nvtnBB1kScz6tI6SvlkwgPOvHneBX-CHiDHPAlS_GOsnh1j5hVn1eRMbPS728sQpsTlVJ4WOpDP9AO1u4JG2ViU-4gohtpN5Lkc7FFbz30MSpi3aQQxXRjHslA--4Hbc-fqD1TjqgUyNfF4xK_paSUgihHwygIqNUeI6MuCltKKJCUR4eeNXItXPl9_GxSsWPYpetIw-0yFHwdrTWvr4fmy-gdteNrAtOv6DwDvGsgw52vnqoX8Sev_yWg9FBGKnFcRLIyMfkw9_7UQEn0-P9v0kqLxf63xk3QnGtdlhZNLA8l3OapCpHkYeZgcEX9UQFU";
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
                            String header = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjY4MDFmZjE1NTdlNWJhMzkzY2RlM2U0NjRiMGM0MzQ3MDkwNWYzMDYwZTZhZDFkOTQ4NTIyY2I3NDI2YjY0ZDFmODBiOWJkODY0YmEyM2UxIn0.eyJhdWQiOiIyIiwianRpIjoiNjgwMWZmMTU1N2U1YmEzOTNjZGUzZTQ2NGIwYzQzNDcwOTA1ZjMwNjBlNmFkMWQ5NDg1MjJjYjc0MjZiNjRkMWY4MGI5YmQ4NjRiYTIzZTEiLCJpYXQiOjE1NjEzNjI2ODcsIm5iZiI6MTU2MTM2MjY4NywiZXhwIjoxNTYxNDQ5MDg3LCJzdWIiOiIxIiwic2NvcGVzIjpbIioiXX0.RXUUxeLvYkkk1V-pmu-120N5JejjaDmTfG0zO0Zu3lMc5OChjlSvDiKm2jW6geCIp2gZeOrkC4HBNpSngjKue_v1l1UyyYudOofTZV3DUlF-hwhhwMJ2RKxp6yq2ecGfxCcg3ZED1dp0dAjmqmNCGUZViykQctSQC7FI3KXQeL-96wQj6G9YnN0n2sVOkeH2m1AYR2YjkXFW3C-lMujiqbfoH0i_DyRWqmvnH4IS67L8Ec0dWBNgWbDWyrO6Za6z9Im6VHfeqVkVYbvFdKrN8mtNuQQ0oioG_6vvuLE9zV-p2YT1t_WogqieFJHb9C6t9QZCqDopU7QBKiczoSk72tMffL0j_Byn1TlG7TlN0nvtnBB1kScz6tI6SvlkwgPOvHneBX-CHiDHPAlS_GOsnh1j5hVn1eRMbPS728sQpsTlVJ4WOpDP9AO1u4JG2ViU-4gohtpN5Lkc7FFbz30MSpi3aQQxXRjHslA--4Hbc-fqD1TjqgUyNfF4xK_paSUgihHwygIqNUeI6MuCltKKJCUR4eeNXItXPl9_GxSsWPYpetIw-0yFHwdrTWvr4fmy-gdteNrAtOv6DwDvGsgw52vnqoX8Sev_yWg9FBGKnFcRLIyMfkw9_7UQEn0-P9v0kqLxf63xk3QnGtdlhZNLA8l3OapCpHkYeZgcEX9UQFU";
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
