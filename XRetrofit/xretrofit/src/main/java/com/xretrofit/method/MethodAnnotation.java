package com.xretrofit.method;

import java.util.List;

/**
 * @author bsnl_yanxuwen
 * @date 2021/2/4 14:00
 * Description :
 * 方法注解，就是那些get post
 */
public class MethodAnnotation {
    private Class annotation;//注解类型
    private List<Object> key;//注解key

    public MethodAnnotation(Class annotation , List<Object> key){
        this.annotation = annotation;
        this.key = key;

    }

    public Class getAnnotation() {
        return annotation;
    }

    /**
     * key虽然可以多个，但是这个基本都显示1个，并且为String
     */
    public String getKey() {
        if (key == null || key.isEmpty()){
            return null;
        }
        if (key.get(0) instanceof String){
            return (String) key.get(0);
        }
        return null;
    }

    /**
     * @Headers 使用
     */
    public List<Object> getKeys() {
        return key;
    }
}
