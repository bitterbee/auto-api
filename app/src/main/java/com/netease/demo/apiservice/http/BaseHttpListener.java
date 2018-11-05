package com.netease.demo.apiservice.http;

/**
 * Created by zyl06 on 2018/11/5.
 */

public interface BaseHttpListener {
    void onHttpSuccess(String httpName, Object result);

    void onHttpError(String httpName, int errorCode, String errorMsg);
}
