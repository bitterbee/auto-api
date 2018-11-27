package com.netease.demo.autoapi.auto_api.factory;

import com.netease.demo.autoapi.auto_api.AppCalculator;

/**
 * com.netease.demo.autoapi.Calculator api Class's factory Interface
 */
public interface AppCalculatorApiFactory {
  AppCalculator newInstance();

  AppCalculator newWithIncr(int incre);
}
