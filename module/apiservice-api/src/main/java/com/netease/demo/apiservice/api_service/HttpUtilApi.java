package com.netease.demo.apiservice.api_service;

import com.netease.libs.api_service.ApiBase;

/**
 * com.netease.demo.apiservice.http.HttpUtil 的 api 接口
 */
public interface HttpUtilApi extends ApiBase {
  RequestApi query(HttpListenerApi listener);

  RequestApi queryNoCallback(HttpListenerApi listener);
}
