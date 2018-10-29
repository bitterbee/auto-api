package com.netease.demo.apiservice.api_service.factory;

import com.netease.demo.apiservice.api_service.AppCalculator;

/**
 * com.netease.demo.apiservice.Calculator api 类的工厂接口
 */
public interface AppCalculatorApiFactory {
  AppCalculator newInstance();

  AppCalculator newWithIncr(int incre);
}
