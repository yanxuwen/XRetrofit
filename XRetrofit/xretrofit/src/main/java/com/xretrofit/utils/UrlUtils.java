package com.xretrofit.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UrlUtils {
    /**
     * @param url    实际URL的path
     * @param params
     * @return
     */
    public static String urlJoint(String url, Map<String, Object> params) {
        StringBuilder realURL = new StringBuilder();
        realURL = realURL.append(url);
        boolean isFirst = true;
        if (params == null) {
            params = new HashMap<>();
        } else {
            Set<Map.Entry<String, Object>> entrySet = params.entrySet();
            for (Map.Entry<String, Object> entry : entrySet) {
                if (isFirst && !url.contains("?")) {
                    isFirst = false;
                    realURL.append("?");
                } else {
                    realURL.append("&");
                }
                realURL.append(entry.getKey());
                realURL.append("=");
                if (entry.getValue() == null) {
                    realURL.append(" ");
                } else {
                    realURL.append(entry.getValue());
                }

            }
        }

        return realURL.toString();
    }
}
