package com.netease.demo.apiservice.api_service;

import com.netease.libs.api_service.ApiBase;

/**
 * com.netease.demo.apiservice.test.SharedWzpCommonRequestTask 的 api 接口
 */
public interface SharedWzpCommonRequestTaskApi extends ApiBase {
  RequestApi query(HttpListenerApi listener);

  RequestApi queryArray(HttpListenerApi listener);

  boolean cancel();
}
