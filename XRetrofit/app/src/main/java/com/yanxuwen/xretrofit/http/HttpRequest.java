package com.yanxuwen.xretrofit.http;

import com.xretrofit.HttpManager;

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
