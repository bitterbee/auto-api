package com.netease.demo.apiservice.http;

import com.netease.libs.apiservice.anno.ApiServiceClassAnno;

/**
 * Created by zyl06 on 2018/11/5.
 */
@ApiServiceClassAnno(allPublicNormalApi = true)
public interface HttpListener {

    void onHttpSuccess(String httpName, Object result);

    void onHttpError(String httpName, int errorCode, String errorMsg);
}
