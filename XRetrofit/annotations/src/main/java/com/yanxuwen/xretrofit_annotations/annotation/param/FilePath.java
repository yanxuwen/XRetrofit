//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.yanxuwen.xretrofit_annotations.annotation.param;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//用于文件上传用的路径
//支持 File String List<File> List<String>
@Documented
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.CLASS)
public @interface FilePath {
}
