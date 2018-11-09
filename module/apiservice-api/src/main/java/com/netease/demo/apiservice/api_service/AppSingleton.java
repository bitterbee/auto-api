package com.netease.demo.apiservice.api_service;

import com.netease.libs.api_service.ApiBase;
import java.lang.String;

/**
 * com.netease.demo.apiservice.Singleton's api Interface
 */
public interface AppSingleton extends ApiBase {
  String foo1(String str1, String str2);

  void foo2();

  void foo3();
}
