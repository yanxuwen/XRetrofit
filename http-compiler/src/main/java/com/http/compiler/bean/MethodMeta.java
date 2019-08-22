//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.http.compiler.bean;
import java.util.List;
import java.util.Map;
import javax.lang.model.type.TypeMirror;

/**
 * 方法
 */
public class MethodMeta {
    private String url;
    private String name;
    private boolean deal;//是否特殊处理
    private long timeout;//超时时间
    private TypeMirror returnType;
    /**
     * 请求类型
     */
    private int requestType;
    private TypeMirror result;
    private Map<String, String> headers;
    private List<ParamMeta> params;

    public @interface TYPE {
        public int TYPE_GET = 1;
        public int TYPE_POST = 2;
        public  int TYPE_PUT = 3;
        public  int TYPE_DELETE = 4;
        public int TYPE_DOWNLOAD = 5;
        public int TYPE_UPLOAD = 6;
    }

    public MethodMeta() {
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public boolean isDeal() {
        return deal;
    }

    public void setDeal(boolean deal) {
        this.deal = deal;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public TypeMirror getReturnType() {
        return returnType;
    }

    public void setReturnType(TypeMirror returnType) {
        this.returnType = returnType;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TypeMirror getResult() {
        return this.result;
    }

    public void setResult(TypeMirror result) {
        this.result = result;
    }

    public List<ParamMeta> getParams() {
        return this.params;
    }

    public void setParams(List<ParamMeta> params) {
        this.params = params;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }
}
