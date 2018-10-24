package com.netease.demo.apiservice.api_service;

import java.util.Timer;

/**
 * com.netease.demo.apiservice.FunctionUtil 的 api 接口
 */
public interface FunctionUtilApi {
  long getSDAvailableSize();

  void doNoReturn();

  int add(int a, int b);

  Timer getTimer();
}
