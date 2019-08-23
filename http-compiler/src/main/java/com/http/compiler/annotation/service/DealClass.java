package com.http.compiler.annotation.service;

import com.http.compiler.HttpDealMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface DealClass {
    Class<? extends HttpDealMethod> value();

    boolean encoded() default false;
}
