package com.netease.demo.apiservice.api_service;

import com.netease.libs.api_service.ApiBase;
import java.lang.String;

/**
 * com.netease.demo.apiservice.Singleton 的 api 接口
 */
public interface AppSingleton extends ApiBase {
  String foo1(String str1, String str2);

  void foo2();

  void foo3();
}
