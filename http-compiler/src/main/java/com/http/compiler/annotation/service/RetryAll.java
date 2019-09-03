package com.http.compiler.annotation.service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 失败后重试次数
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface RetryAll {
    int value();
    boolean encoded() default false;
}
