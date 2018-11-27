package com.netease.demo.autoapi.auto_api;

import com.netease.libs.auto_api.ApiBase;
import java.util.Timer;

/**
 * com.netease.demo.autoapi.Calculator's api Interface
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
