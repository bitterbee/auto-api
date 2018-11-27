package com.netease.demo.autoapi.auto_api;

import com.netease.libs.auto_api.ApiBase;

/**
 * com.netease.demo.autoapi.test.SharedWzpCommonRequestTask's api Interface
 */
public interface SharedWzpCommonRequestTaskApi extends ApiBase {
  RequestApi query(HttpListenerApi listener);

  RequestApi queryArray(HttpListenerApi listener);
}
