package com.netease.demo.apiservice.api_service;

import com.netease.libs.api_service.ApiBase;

/**
 * com.netease.demo.apiservice.http.HttpUtil's api Interface
 */
public interface HttpUtilApi extends ApiBase {
  RequestApi query(HttpListenerApi listener);

  RequestApi queryNoCallback(HttpListenerApi listener);
}
