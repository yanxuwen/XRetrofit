package com.yanxuwen.myhttpservice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.http.api.DataCallBack;
import com.http.api.NetError;
import com.yanxuwen.myhttpservice.bean.LoginBuild;
import com.yanxuwen.myhttpservice.http.HttpRequest;

public class MainActivity extends AppCompatActivity {

    private TextView tv_status;

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
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onHttpFail(NetError netError) {
                Toast.makeText(MainActivity.this, netError.getMessage(), Toast.LENGTH_LONG).show();
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
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onHttpFail(NetError netError) {
                Toast.makeText(MainActivity.this, netError.getMessage(), Toast.LENGTH_LONG).show();
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
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onHttpFail(NetError netError) {
                Toast.makeText(MainActivity.this, netError.getMessage(), Toast.LENGTH_LONG).show();
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
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onHttpFail(NetError netError) {
                Toast.makeText(MainActivity.this, netError.getMessage(), Toast.LENGTH_LONG).show();
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
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onHttpFail(NetError netError) {
                Toast.makeText(MainActivity.this, netError.getMessage(), Toast.LENGTH_LONG).show();
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
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onHttpFail(NetError netError) {
                Toast.makeText(MainActivity.this, netError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onTest(View view) {
        String reqcode = "10960";
        String pageNo = "1";
        String pageSize = "20";
        String schoolId = "XWQMI77m9Fbv1GWkBba";
        HttpRequest.getNetService().onDeal(reqcode,pageNo,pageSize,schoolId , new DataCallBack<String>(String.class) {
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
}
