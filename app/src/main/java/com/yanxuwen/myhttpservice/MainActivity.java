package com.yanxuwen.myhttpservice;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.http.DataCallBack;
import com.http.api.NetError;
import com.http.api.ProgressCallBack;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.yanxuwen.myhttpservice.bean.LoginBuild;
import com.yanxuwen.myhttpservice.http.HttpRequest;

import okhttp3.Call;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    long time = 0;

    /**
     * get请求
     */
    public void onGet(View view) {
        HttpRequest.getNetService().get(0, 10, "recommend", new DataCallBack<String>(String.class,this) {
            @Override
            public void onHttpStart(final Call call) {
                super.onHttpStart(call);
            }

            @Override
            public void onHttpSuccess(String result) {
                Log.e("yxw", "onGet :" + result);
            }

            @Override
            public void onHttpFail(NetError netError) {
                Log.e("yxw", "onGet fail :" + netError.getMessage());
            }
        });
    }

    /**
     * get请求(URL中带有参数,也支持post)
     */
    public void onGet2(View view) {
        HttpRequest.getNetService().get("v1", 0, 10, "recommend", new DataCallBack<String>(String.class,this) {
            @Override
            public void onHttpSuccess(String result) {
                Log.e("yxw", "onGet2 :" + result);
            }

            @Override
            public void onHttpFail(NetError netError) {
                Log.e("yxw", "onGet2 fail :" + netError.getMessage());
            }
        });
    }

    /**
     * 表单提交
     */
    public void postForm(View view) {
        HttpRequest.getNetService().postForm("10960", new DataCallBack<String>(String.class,this) {
            @Override
            public void onHttpSuccess(String result) {
                Log.e("yxw", "postForm :" + result);
            }

            @Override
            public void onHttpFail(NetError netError) {
                Log.e("yxw", "postForm fail :" + netError.getMessage());
            }
        });
    }

    /**
     * json提交
     */
    public void postJson(View view) {
        String mobile = "15060568265";
        String password = "e10adc3949ba59abbe56e057f20f883e";
        HttpRequest.getNetService().postJson(mobile, password, new DataCallBack<String>(String.class,this) {
            @Override
            public void onHttpSuccess(String result) {
                Log.e("yxw", "postJson :" + result);
            }

            @Override
            public void onHttpFail(NetError netError) {
                Log.e("yxw", "postJson fail :" + netError.getMessage());
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
        HttpRequest.getNetService().postJson(json, new DataCallBack<String>(String.class,this) {
            @Override
            public void onHttpSuccess(String result) {
                Log.e("yxw", "postJson :" + result);
            }

            @Override
            public void onHttpFail(NetError netError) {
                Log.e("yxw", "postJson fail :" + netError.getMessage());
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
        HttpRequest.getNetService().postJson(mLoginBuild, new DataCallBack<String>(String.class,this) {
            @Override
            public void onHttpSuccess(String result) {
                Log.e("yxw", "postJson3 :" + result);
            }

            @Override
            public void onHttpFail(NetError netError) {
                Log.e("yxw", "postJson3 fail :" + netError.getMessage());
            }
        });
    }

    /**
     * put 提交
     */
    public void onPut(View view) {
        HttpRequest.getNetService().put("header", "测试", "signature", "area", new DataCallBack<String>(String.class,this) {
            @Override
            public void onHttpSuccess(String result) {
                Log.e("yxw", "onPut :" + result);
            }

            @Override
            public void onHttpFail(NetError netError) {
                Log.e("yxw", "onPut fail :" + netError.getMessage());
            }
        });
    }

    /**
     * delete 提交
     */
    public void onDelete(View view) {
        HttpRequest.getNetService().delete("header", "123231", new DataCallBack<String>(String.class,this) {
            @Override
            public void onHttpSuccess(String result) {
                Log.e("yxw", "onDelete :" + result);
            }

            @Override
            public void onHttpFail(NetError netError) {
                Log.e("yxw", "onDelete fail :" + netError.getMessage());
            }
        });
    }

    public void onDownload(View v) {
        //先权限申请，在请求
        RxPermissions.getInstance(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        if (granted) {
                            String path = Environment.getExternalStorageDirectory().toString() + "/测试";//获取目录
                            HttpRequest.getNetService().download(path, "text.apk", new ProgressCallBack() {

                                @Override
                                public void onHttpSuccess(Object result) {
                                    Log.e("yxw", "onDownload :" + result);
                                }

                                @Override
                                public void onHttpFail(NetError netError) {
                                    Log.e("yxw", "onDownload fail :" + netError.getMessage());
                                }

                                @Override
                                public void onLoadProgress(float progress) {
                                    Log.e("yxw", "onDownload progress:" + progress);
                                }
                            });
                        } else {
                            Toast.makeText(MainActivity.this, "请打开存储权限", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void onUpload(View v) {
        //先权限申请，在请求
        RxPermissions.getInstance(this).request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        if (granted) {
                            //允许权限
                            String path = Environment.getExternalStorageDirectory().toString() + "/huawei/MagazineUnlock/magazine-unlock-01-2.3.1344-_132FEBAC9815C7732FE627DD6380E5CA.jpg";//获取跟目录
                            String header = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImEyYmM2NDg0ZWJjZjBmM2M1ZDM2MTc0YzdiMzg0YzQ3NDRhMDVjNmU2NDcyODkwYjViOGIwYzYxOWRhMTYzYTU4NzNmYjdjYzczNTIyYWY1In0.eyJhdWQiOiIyIiwianRpIjoiYTJiYzY0ODRlYmNmMGYzYzVkMzYxNzRjN2IzODRjNDc0NGEwNWM2ZTY0NzI4OTBiNWI4YjBjNjE5ZGExNjNhNTg3M2ZiN2NjNzM1MjJhZjUiLCJpYXQiOjE1NjY0NTk2MDMsIm5iZiI6MTU2NjQ1OTYwMywiZXhwIjoxNTY2NTQ2MDAyLCJzdWIiOiIxIiwic2NvcGVzIjpbIioiXX0.QShI94S6uwOcx4gEhyZyJ9rkmj3BkchoOv1rWFlHcQ2PmfsiNcRMs_1DXFCXxj6QZ7eqCR6noO4AjF67WiYPhr7_oVVtnHUc3BBHJjGYc8KNdkwGA9p14HhjL5Ngvd-Pew0iI1pUD8boETFTGrLP2B4pAxv1kUxEtyKULIPxNM_6cPK1hGA0lBtVissflH0vwmtqQzNvdDfqX7MwA1nSoXaut_BS9zGkNTh9yqlOXBIXmpvW2pPvSWfyVX2la9Z7URToEfzrh8N9kQ_df_v6e0SFXl1lCVqWf61cI_57_s6zKEn93Id0YvEVAYgCeZfLNbvfcVuW32ijU5gvfzsPBu6gqjprIn-9uxfgeXokku_DGMJkILF95_6bxNIa9gmS1umD8TH-7s_nhVZzEs12jtcHPtIuKcEb-a8HER31GJE01TxKVsRlVqdG1CpL6lFn0uYW0EjSeDMlaTCO5LjVYvN8YX87iLRZmiIPGvkvIa2pq50YV-pOioT9ur7CJhaFrhCxW9LLz8XMi8Ps6588ZV5cP8U0sDaLJBHGvuf999IfpeIGUpBE1VHo0ERA8ogn6kfYoOLysqTEucevpODWTZyHNSXO4Qc-3v-kGStjrQvjbzgTTb3JPqMedDFG9qYNvkhTU8xd70hBGXrTwSLpthMipxIsxElm2K93a7sVJBk";
                            HttpRequest.getNetService().upload(header, new String[]{path, path}, new String[]{"file", "file"}, new String[]{"test.jpg", "test2.jpg"}, new ProgressCallBack() {
                                @Override
                                public void onHttpSuccess(Object result) {
                                    Log.e("yxw", "onUpload :" + result);
                                }

                                @Override
                                public void onHttpFail(NetError netError) {
                                    Log.e("yxw", "onUpload fail :" + netError.getMessage());
                                }

                                @Override
                                public void onLoadProgress(float progress) {
                                    Log.e("yxw", "onUpload progress:" + progress);
                                }
                            });
                        } else {
                            Toast.makeText(MainActivity.this, "请打开存储权限", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void onUpload2(View v) {
        //先权限申请，在请求
        RxPermissions.getInstance(this).request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        if (granted) {
                            //允许权限
                            String path = Environment.getExternalStorageDirectory().toString() + "/huawei/MagazineUnlock/magazine-unlock-01-2.3.1344-_132FEBAC9815C7732FE627DD6380E5CA.jpg";//获取跟目录
                            String header = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImEyYmM2NDg0ZWJjZjBmM2M1ZDM2MTc0YzdiMzg0YzQ3NDRhMDVjNmU2NDcyODkwYjViOGIwYzYxOWRhMTYzYTU4NzNmYjdjYzczNTIyYWY1In0.eyJhdWQiOiIyIiwianRpIjoiYTJiYzY0ODRlYmNmMGYzYzVkMzYxNzRjN2IzODRjNDc0NGEwNWM2ZTY0NzI4OTBiNWI4YjBjNjE5ZGExNjNhNTg3M2ZiN2NjNzM1MjJhZjUiLCJpYXQiOjE1NjY0NTk2MDMsIm5iZiI6MTU2NjQ1OTYwMywiZXhwIjoxNTY2NTQ2MDAyLCJzdWIiOiIxIiwic2NvcGVzIjpbIioiXX0.QShI94S6uwOcx4gEhyZyJ9rkmj3BkchoOv1rWFlHcQ2PmfsiNcRMs_1DXFCXxj6QZ7eqCR6noO4AjF67WiYPhr7_oVVtnHUc3BBHJjGYc8KNdkwGA9p14HhjL5Ngvd-Pew0iI1pUD8boETFTGrLP2B4pAxv1kUxEtyKULIPxNM_6cPK1hGA0lBtVissflH0vwmtqQzNvdDfqX7MwA1nSoXaut_BS9zGkNTh9yqlOXBIXmpvW2pPvSWfyVX2la9Z7URToEfzrh8N9kQ_df_v6e0SFXl1lCVqWf61cI_57_s6zKEn93Id0YvEVAYgCeZfLNbvfcVuW32ijU5gvfzsPBu6gqjprIn-9uxfgeXokku_DGMJkILF95_6bxNIa9gmS1umD8TH-7s_nhVZzEs12jtcHPtIuKcEb-a8HER31GJE01TxKVsRlVqdG1CpL6lFn0uYW0EjSeDMlaTCO5LjVYvN8YX87iLRZmiIPGvkvIa2pq50YV-pOioT9ur7CJhaFrhCxW9LLz8XMi8Ps6588ZV5cP8U0sDaLJBHGvuf999IfpeIGUpBE1VHo0ERA8ogn6kfYoOLysqTEucevpODWTZyHNSXO4Qc-3v-kGStjrQvjbzgTTb3JPqMedDFG9qYNvkhTU8xd70hBGXrTwSLpthMipxIsxElm2K93a7sVJBk";
                            HttpRequest.getNetService().upload(header, path, "file", "test.jpg", new ProgressCallBack() {

                                public void onHttpStart(final Call call) {
                                    super.onHttpStart(call);
                                    time = System.currentTimeMillis();
                                }

                                @Override
                                public void onHttpSuccess(Object result) {
                                    Log.e("yxw", "onUpload2 :" + result);
                                }

                                @Override
                                public void onHttpFail(NetError netError) {
                                    Log.e("yxw", "onUpload2 fail :" + netError.getMessage()  + ",time:" + (System.currentTimeMillis() - time));
                                }

                                @Override
                                public void onLoadProgress(float progress) {
                                    Log.e("yxw", "onUpload2 progress:" + progress);
                                }
                            });
                        } else {
                            Toast.makeText(MainActivity.this, "请打开存储权限", Toast.LENGTH_SHORT).show();
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
        HttpRequest.getNetService().onDeal(reqcode, pageNo, pageSize, schoolId, new DataCallBack<String>(String.class,this) {
            @Override
            public void onHttpSuccess(String result) {
                Log.e("yxw", "onTest :" + result);
            }

            @Override
            public void onHttpFail(NetError netError) {
                Log.e("yxw", "onTest fail :" + netError.getMessage());
            }
        });
    }
}
