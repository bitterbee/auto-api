package com.netease.demo.apiprovider.api_provider;

import java.util.Timer;

/**
 * com.netease.demo.apiprovider.FunctionUtil 的 api 接口
 */
public interface demoFunction {
  long getSDAvailableSize();

  void doNoReturn();

  int add(int a, int b);

  Timer getTimer();
}
