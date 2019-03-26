package com.netease.demo.autoapi.auto_api;

import com.netease.libs.auto_api.ApiBase;
import java.lang.String;

/**
 * com.netease.demo.autoapi.Singleton's api Interface
 */
public interface SingletonApi extends ApiBase {
  String foo1(String str1, String str2);

  void foo2();

  void foo3();
}
