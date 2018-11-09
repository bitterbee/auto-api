package com.netease.demo.apiservice.api_service;

import com.netease.libs.api_service.ApiBase;

/**
 * com.netease.demo.apiservice.FunctionUtil's api Interface
 */
public interface AppFunction extends ApiBase {
  long getSDAvailableSize();

  void noReturn();
}
