package com.http.compiler;

import com.http.compiler.bean.CallBack;
import com.http.compiler.bean.DealParams;

import java.io.Serializable;

/**
 *  统一处理方法跟回调
 */
public interface HttpDealMethod{
    /**
     * 处理请求
     * 如果处理后，各种字段都有，则会优先表单请求，然后再者json请求
     * 注意一定要return dealParams 不然不处理
     */
    public DealParams dealRequest(DealParams dealParams);
    /**
     * 如果要设置返回错误，则new CallBack(-1,"请求失败") ，第一个参数不能为0即可，0代表成功
     * 如果要请求成功，直接 new CallBack(json),
     * 但是如果你的httpCode不是200，那么不管这边设置成功，还是会返回错误，
     * 但是设置错误的话 优先级 new CallBack(-1,"请求失败") > httpCode 的错误
     * return null 则不做任何处理
     * @param httpCode http 请求code
     * @param str 接口返回字符串，，如果是文件下载，则返回保存路径
     * @return
     */
    public CallBack dealCallBack(int httpCode , String str);
}
