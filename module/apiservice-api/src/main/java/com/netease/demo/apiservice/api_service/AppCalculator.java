package com.netease.demo.apiservice.api_service;

import java.util.Timer;

/**
 * com.netease.demo.apiservice.Calculator 的 api 接口
 */
public interface AppCalculator {
  int appMinus(int a, int b);

  int add(int a, int b);

  Timer getTimer();

  double nonStaticAdd(double a, double b);

  int increase(int a);

  int decrease(int a);
}
