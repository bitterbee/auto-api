package com.netease.demo.autoapi.auto_api;

import com.netease.libs.auto_api.ApiBase;

/**
 * com.netease.demo.autoapi.test1.Calculator's api Interface
 */
public interface CalculatorAlias extends ApiBase {
  int minuse(int a, int b);

  int multiply(int a, int b);

  int add(int a, int b);
}
