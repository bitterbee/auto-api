package com.netease.demo.apiservice.api_service.factory;

import com.netease.demo.apiservice.api_service.AppCalculator;

/**
 * com.netease.demo.apiservice.Calculator api Class's factory Interface
 */
public interface AppCalculatorApiFactory {
  AppCalculator newInstance();

  AppCalculator newWithIncr(int incre);
}
