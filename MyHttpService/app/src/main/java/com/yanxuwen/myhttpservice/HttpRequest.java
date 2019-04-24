package com.yanxuwen.myhttpservice;
import com.yanxuwen.compiler.ElementUtils;

public class HttpRequest {

    private static NetService netService;

    public static NetService getNetService() {
        try {
            if (netService == null) {
                synchronized (HttpRequest.class) {
                    if (netService == null) {
                        netService = (NetService) Class.forName(ElementUtils.getImplName(NetService.class))
                                .getConstructor().newInstance();
                        netService.setHttpDealMethod(new HttpDealMethodImpl());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return netService;
    }
}
