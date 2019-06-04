//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.http.compiler.bean;

import java.util.List;

/**
 * 类信息
 */
public class ServiceMeta {
    private String baseUrl;
    private String packageName;
    private String sampleName;
    private List<MethodMeta> methodMetas;
    private String dealclassName;//处理类的路径名

    public ServiceMeta() {
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getSampleName() {
        return this.sampleName;
    }

    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }

    public List<MethodMeta> getMethodMetas() {
        return this.methodMetas;
    }

    public void setMethodMetas(List<MethodMeta> methodMetas) {
        this.methodMetas = methodMetas;
    }

    public String getBaseUrl() {
        return baseUrl == null ? "" : baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getDealclassName() {
        return dealclassName;
    }

    public void setDealclassName(String dealclassName) {
        this.dealclassName = dealclassName;
    }
}
