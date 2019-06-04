package com.yanxuwen.myhttpservice.http;
import com.http.compiler.ElementUtils;
public class HttpRequest {

    private static NetService netService;

    public static NetService getNetService() {
        try {
            if (netService == null) {
                synchronized (HttpRequest.class) {
                    if (netService == null) {
                        netService = (NetService) Class.forName(ElementUtils.getImplName(NetService.class))
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
