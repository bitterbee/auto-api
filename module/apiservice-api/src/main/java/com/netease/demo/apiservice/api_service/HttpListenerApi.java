package com.netease.demo.apiservice.api_service;

import com.netease.libs.api_service.ApiBase;
import java.lang.Object;
import java.lang.String;

/**
 * com.netease.demo.apiservice.http.HttpListener's api Interface
 */
public interface HttpListenerApi extends ApiBase {
  void onCancel();

  void onHttpSuccess(String httpName, Object result);

  void onHttpError(String httpName, int errorCode, String errorMsg);
}
