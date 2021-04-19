package com.xretrofit.compiler;

/**
 * 这个类改版的话，HttpManager里面的getImplName 也需要改变
 */
@SuppressWarnings("unchecked")
public class ElementUtils {
    public static String packageName = "com.http";

    public static String getImplName(Class<?> clazz) {
        return packageName + "." + clazz.getSimpleName() + "$Impl";
    }
}
