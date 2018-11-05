package com.netease.demo.apiservice.api_service;

import com.netease.libs.api_service.ApiBase;
import java.util.Timer;

/**
 * com.netease.demo.apiservice.Calculator 的 api 接口
 */
public interface AppCalculator extends ApiBase {
  int appMinus(int a, int b);

  int add(int a, int b);

  float add(float a, float b);

  Timer getTimer();

  double nonStaticAdd(double a, double b);

  int increase(int a);

  int decrease(int a);

  int add(AppDataModel model);

  AppDataModel add(AppDataModel a, AppDataModel b);
}
