package com.netease.demo.apiservice.api_service;

import com.netease.libs.api_service.ApiBase;
import java.lang.Object;
import java.lang.String;

/**
 * com.netease.demo.apiservice.HttpListener 的 api 接口
 */
public interface HttpListenerApi extends ApiBase {
  void onHttpSuccess(String httpName, Object result);

  void onHttpError(String httpName, int errorCode, String errorMsg);
}
