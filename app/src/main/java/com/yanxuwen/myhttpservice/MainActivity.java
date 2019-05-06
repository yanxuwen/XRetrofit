package com.yanxuwen.myhttpservice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tv_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_status = findViewById(R.id.tv_status);
//        String json = "{\n" +
//                "\t\"appType\": \"APP_TYPE_PARENT\",\n" +
//                "\t\"code\": \"EF18B7A41E3C4D999DE02883B399C027\",\n" +
//                "\t\"devKey\": \"d9aec213d74127f2c20cfa4ebe6fcaa7\",\n" +
//                "\t\"devType\": \"Android\",\n" +
//                "\t\"loginType\": \"NORMAL\",\n" +
//                "\t\"oldDevKey\": \"863968043750920\",\n" +
//                "\t\"pushKey\": \"\",\n" +
//                "\t\"release\": \"1\"\n" +
//                "}";
//        HttpRequest.getNetService().post5(json,new DataCallBack<TokenBean>(TokenBean.class){
//            @Override
//            public void onHttpSuccess(TokenBean result) {
//                Log.e("yxw","");
//            }
//
//            @Override
//            public void onHttpFail(NetError netError) {
//                Log.e("yxw",netError.getMessage());
//
//            }
//        });
    }

    public void onClick(View view) {
        String json = "{\"pageNo\":1,\"pageSize\":20,\"studentId\":\"e94a938d8414f9ac005e\"}";
//        HttpRequest.getNetService().post6(json, new DataCallBack<Test>(Test.class) {
//
//            @Override
//            public void onHttpSuccess(Test result) {
//                tv_status.setText(result.getRegisterList());
//            }
//
//            @Override
//            public void onHttpFail(NetError netError) {
//                tv_status.setText(netError.getMessage());
//            }
//        });
    }
}
