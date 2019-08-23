package com.http.api.bean;

import com.http.api.BaseDataCallBack;
import com.http.compiler.bean.DealParams;

import java.util.HashMap;

/**
 * 自动生成用的请求参数
 */
public class RequestParams extends DealParams implements Cloneable {

    private BaseDataCallBack callback;

    public BaseDataCallBack getCallback() {
        return callback;
    }

    public void setCallback(BaseDataCallBack callback) {
        this.callback = callback;
    }


    @Override
    public Object clone() {
        RequestParams bean = null;
        try {
            bean = (RequestParams)super.clone();
//          经测试，ma无法拷贝，所以要在拷贝一次
            if (bean.getHeaders() instanceof HashMap){
                bean.setHeaders((HashMap<String, String>) ((HashMap) bean.getHeaders()).clone());
            }
            if (bean.getMapField() instanceof HashMap){
                bean.setMapField((HashMap<String, String>) ((HashMap) bean.getMapField()).clone());
            }
            if (bean.getParams() instanceof HashMap){
                bean.setParams((HashMap<String, Object>) ((HashMap) bean.getParams()).clone());
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return bean;
    }
}
