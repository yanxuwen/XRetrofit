package com.http.api;

/**
 *  统一处理方法跟回调
 */
public interface HttpDealMethod {
    /**
     * 处理请求
     * 如果处理后，各种字段都有，则会优先表单请求，然后再者json请求
     * 注意一定要return dealParams 不然不处理
     */
    public DealParams dealRequest(DealParams dealParams);
    /**
     * 如果要设置返回错误，则new CallBack(-1,"请求失败") ，第一个参数不能为0即可，0代表成功
     * 如果要请求成功，直接 new CallBack(json)
     * return null 则不做任何处理
     */
    public CallBack dealCallBack(String str);
}
