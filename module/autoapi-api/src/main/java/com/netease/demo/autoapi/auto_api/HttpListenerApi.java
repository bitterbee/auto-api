package com.netease.demo.autoapi.auto_api;

import com.netease.libs.auto_api.ApiBase;
import java.lang.Object;
import java.lang.String;

/**
 * com.netease.demo.autoapi.http.HttpListener's api Interface
 */
public interface HttpListenerApi extends ApiBase {
  void onCancel();

  void onHttpSuccess(String httpName, Object result);

  void onHttpError(String httpName, int errorCode, String errorMsg);
}
