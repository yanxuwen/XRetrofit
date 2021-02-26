package com.xretrofit.method;

import java.util.List;

/**
 * @author bsnl_yanxuwen
 * @date 2021/2/4 14:00
 * Description :
 * 参数注解, Queue,Path
 */
public class ParamAnnotation {
    private Class annotation;//注解类型
    private List<Object> key;//注解key ,会存在第一个key
    private Object value;//注解字段，会存在为空问题

    public ParamAnnotation(Class annotation, List<Object> key, Object value) {
        this.annotation = annotation;
        this.key = key;
        this.value = value;
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

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
