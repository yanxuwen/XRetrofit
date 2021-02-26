package com.xretrofit.converter;

import com.xretrofit.Interceptor.DownloadResponseBody;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.ResponseBody;

/**
 * @author bsnl_yanxuwen
 * @date 2021/2/4 17:09
 * Description :
 * 接口数据转换
 * 返回接口的   下载操作
 */
public class DownloadResponseConverter<T> implements Converter<ResponseBody, T> {

    @Override
    public T convert(ResponseBody value) throws Exception {

        DownloadResponseBody responseBody = null;
        if (value instanceof DownloadResponseBody) {
            responseBody = (DownloadResponseBody) value;
        }
        if (responseBody == null) {
            return null;
        }
        long length = responseBody.contentLength();
        if (length == 0 || length == 1) {
            // 说明文件已经下载完，直接跳转安装就好
            return (T) String.valueOf(responseBody.getFile().getAbsoluteFile());
        }
        if (responseBody instanceof DownloadResponseBody) {
            responseBody.setMaxProgress(length + responseBody.getStartsPoint());
        }
        // 保存文件到本地
        InputStream is = null;
        RandomAccessFile randomAccessFile = null;
        BufferedInputStream bis = null;

        byte[] buff = new byte[2048];
        int len = 0;
        try {
            is = responseBody.byteStream();
            bis = new BufferedInputStream(is);

            // 随机访问文件，可以指定断点续传的起始位置
            randomAccessFile = new RandomAccessFile(responseBody.getFile(), "rwd");
            randomAccessFile.seek( responseBody.getStartsPoint());
            while ((len = bis.read(buff)) != -1) {
                randomAccessFile.write(buff, 0, len);
            }

            // 下载完成
            return (T) String.valueOf(responseBody.getFile().getAbsoluteFile());
        } catch (Exception e) {
//                    postUIFail(e, requestParams);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (bis != null) {
                    bis.close();
                }
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
            } catch (Exception e) {

            }
        }
        return null;
    }
}
