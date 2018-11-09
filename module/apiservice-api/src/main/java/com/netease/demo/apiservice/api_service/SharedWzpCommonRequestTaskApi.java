package com.netease.demo.apiservice.api_service;

import com.netease.libs.api_service.ApiBase;

/**
 * com.netease.demo.apiservice.test.SharedWzpCommonRequestTask's api Interface
 */
public interface SharedWzpCommonRequestTaskApi extends ApiBase {
  RequestApi query(HttpListenerApi listener);

  RequestApi queryArray(HttpListenerApi listener);
}
