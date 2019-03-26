package com.netease.demo.autoapi.auto_api;

import com.netease.libs.auto_api.ApiBase;

/**
 * com.netease.demo.autoapi.Minus's api Interface
 */
public interface MinusApi extends ApiBase {
  int calu();

  int minus(int a, int b);
}
