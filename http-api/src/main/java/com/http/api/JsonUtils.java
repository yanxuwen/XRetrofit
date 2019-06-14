package com.http.api;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class JsonUtils {

    /**
     * @param json  json串
     * @param clazz 返回类型
     * @param str
     * @return
     * @param <T>
     */
    public static <T>T parse(String json,Class<T> clazz , Object... str) {
        try {
            JSONObject jsonObject = null;
            JSONArray jsonArray = null;
            Object o = JSON.parse(json);
            if (o instanceof JSONObject){
                 jsonObject = (JSONObject)o;
            } else if (o instanceof JSONArray){
                 jsonArray = (JSONArray)o;
            } else {
                return parseObject(json, clazz);
            }
            if (str == null || str.equals("") || (jsonObject == null && jsonArray == null)) {
                return parseObject(json, clazz);
            }
            for (int i = 0; i < str.length; i++) {
                if (str[i] instanceof String) {
                    Object object = jsonObject.get(str[i]);
                    if (object instanceof JSONObject ){
                        jsonObject = (JSONObject)object;
                    } else if (object instanceof JSONArray){
                        jsonArray = (JSONArray)object;
                    } else {
                        return parseObject(String.valueOf(object), clazz);
                    }
                } else if (str[i] instanceof Integer || str[i] instanceof Long) {
                    if (jsonArray == null){
                        return parseObject(String.valueOf(jsonObject.toString()), clazz);
                    }
                    Object object = jsonArray.get((int)str[i]);
                    if (object instanceof JSONObject){
                        jsonObject = (JSONObject)object;
                    } else {
                        return parseObject(String.valueOf(object), clazz);
                    }
                } else {
                    return parseObject(jsonObject.toString(), clazz);
                }
            }
            return parseObject(jsonObject.toString(), clazz);
        } catch (Exception e) {
            return null;
        }

    }
    private static <T>T parseObject (String text, Class<T> clazz){
        try {
            T result = JSONObject.parseObject(text, clazz);
            if (result == null){
                text = null;
                throw new NullPointerException("");  //直接手动抛出异常
            }
            return result;
        } catch (Exception e){
            Object o = null;
            if (clazz.equals(String.class)){
                return (T) (text == null ? "" : text);
            } else if (clazz.equals(Integer.class)){
                o = Integer.parseInt(text == null ? "0" : text);
                return (T) o;
            } else if (clazz.equals(Long.class)){
                o = Long.parseLong(text == null ? "0" : text);
                return (T) o;
            } else if (clazz.equals(Boolean.class)){
                o = Boolean.parseBoolean(text == null ? "false" : text);
                return (T) o;
            } else if (clazz.equals(Double.class)){
                o = Double.parseDouble(text == null ? "0" : text);
                return (T) o;
            } else if (clazz.equals(Float.class)){
                o = Float.parseFloat(text == null ? "0" : text);
                return (T) o;
            } else {
                return null;
            }
        }
    }

    public static String toString(Object object) {
        return JSONObject.toJSONString(object);
    }
}
