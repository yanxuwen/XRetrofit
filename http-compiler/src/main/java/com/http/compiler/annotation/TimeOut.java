//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.http.compiler.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
/*单个接口的超时时间，单位毫秒,如果要设置所有接口则使用OkHttp默认配置*/
public @interface TimeOut {
    long value();
    boolean encoded() default false;
}
