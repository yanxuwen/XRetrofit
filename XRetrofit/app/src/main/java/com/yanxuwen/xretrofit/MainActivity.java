package com.yanxuwen.xretrofit;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;
import com.xretrofit.call.Call;
import com.xretrofit.call.ProgressCall;
import com.yanxuwen.xretrofit.bean.HomeInfoV5;
import com.yanxuwen.xretrofit.bean.LoginBuild;
import com.yanxuwen.xretrofit.callback.MyCallBack;
import com.yanxuwen.xretrofit.callback.MyProgressCallBack;
import com.yanxuwen.xretrofit.http.HttpRequest;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

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

    /**
     * get请求(URL中带有参数,也支持post)
     */
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

    /**
     * 表单提交
     */
    public void postForm(View view) {
        Call<String> call = HttpRequest.getNetService().postForm("19906058322", "10960");
        call.enqueue(new MyCallBack<String>() {
            @Override
            public void success(String s) {
                Log.e("yxw", "postForm:" + s);
            }

            @Override
            public void fail(String msg) {
                Log.e("yxw", "postForm:" + msg);
            }
        });
    }

    /**
     * json提交
     */
    public void postJson(View view) {
        String userId = "c053e1a2-7335-4451-9201-e25e805a19f5-1578390821008";
        Call<String> call = HttpRequest.getNetService().postJson(userId);
        call.enqueue(new MyCallBack<String>() {
            @Override
            public void success(String s) {
                Log.e("yxw", "postJson:" + s);
            }

            @Override
            public void fail(String msg) {
                Log.e("yxw", "postJson:" + msg);
            }
        });
    }

    /**
     * json 整串提交
     */
    public void postJson2(View view) {
        String json = "{\n" +
                "\t\"userId\": \"c053e1a2-7335-4451-9201-e25e805a19f5-1578390821008\"\n" +
                "}";
        Call<String> call = HttpRequest.getNetService().postJson2(json);
        call.enqueue(new MyCallBack<String>() {
            @Override
            public void success(String s) {
                Log.e("yxw", "postJson2:" + s);
            }

            @Override
            public void fail(String msg) {
                Log.e("yxw", "postJson2:" + msg);
            }
        });
    }

    /**
     * json 整串提交
     */
    public void postJson3(View view) {
        LoginBuild mLoginBuild = new LoginBuild();
        mLoginBuild.setUserId("c053e1a2-7335-4451-9201-e25e805a19f5-1578390821008");
        Call<String> call = HttpRequest.getNetService().postJson(mLoginBuild);
        call.enqueue(new MyCallBack<String>() {
            @Override
            public void success(String s) {
                Log.e("yxw", "postJson2:" + s);
            }

            @Override
            public void fail(String msg) {
                Log.e("yxw", "postJson2:" + msg);
            }
        });
    }

    /**
     * put 提交
     */
    public void onPut(View view) {
        Call<String> call = HttpRequest.getNetService().put("header", "测试", "signature", "area");
        call.enqueue(new MyCallBack<String>() {
            @Override
            public void success(String s) {
                Log.e("yxw", "onPut :" + s);
            }

            @Override
            public void fail(String msg) {
                Log.e("yxw", "onPut fail :" + msg);
            }
        });
    }

    /**
     * delete 提交
     */
    public void onDelete(View view) {
        Call<String> call = HttpRequest.getNetService().delete("header", "123231");
        call.enqueue(new MyCallBack<String>() {
            @Override
            public void success(String s) {
                Log.e("yxw", "onDelete :" + s);
            }

            @Override
            public void fail(String msg) {
                Log.e("yxw", "onDelete fail :" + msg);
            }
        });
    }

    public void onDownload(View v) {
        //先权限申请，在请求
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.requestEach(
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            String path = Environment.getExternalStorageDirectory().toString() + "/测试/text2.apk";//获取目录
                            ProgressCall<String> call = HttpRequest.getNetService().download(path);
                            call.enqueue(new MyProgressCallBack<String>() {
                                @Override
                                public void onProgress(float progress) {
                                    Log.e("yxw", "onDownload progress :" + progress);
                                }

                                @Override
                                public void success(String s) {
                                    Log.e("yxw", "onDownload  :" + s);
                                }

                                @Override
                                public void fail(String msg) {
                                    Log.e("yxw", "onDownload fail :" + msg);
                                }
                            });
                        } else {
                            Toast.makeText(MainActivity.this, "请打开存储权限", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void onUpload(View v) {
//        //先权限申请，在请求
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.requestEach(
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            //允许权限
                            String path = Environment.getExternalStorageDirectory().toString() + "/测试/测试.mp4";//获取跟目录
                            ProgressCall<String> call = HttpRequest.getNetService().upload(path, true);
                            call.enqueue(new MyProgressCallBack<String>() {
                                @Override
                                public void success(String s) {
                                    Log.e("yxw", "onUpload :" + s);
                                }

                                @Override
                                public void fail(String msg) {
                                    Log.e("yxw", "onUpload2 fail:" + msg);
                                }

                                @Override
                                public void onProgress(float progress) {
                                    Log.e("yxw", "onUpload2 progress:" + progress);
                                }
                            });
                        } else {
                            Toast.makeText(MainActivity.this, "请打开存储权限", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
