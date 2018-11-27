package com.netease.demo.autoapi.auto_api.factory;

import com.netease.demo.autoapi.auto_api.SharedWzpCommonRequestTaskApi;
import java.lang.Class;
import java.lang.Object;
import java.lang.String;
import java.util.Map;

/**
 * com.netease.demo.autoapi.test.SharedWzpCommonRequestTask api Class's factory Interface
 */
public interface SharedWzpCommonRequestTaskApiFactory {
  SharedWzpCommonRequestTaskApi newInstance(String url, Class modelClass, Map<String, String> queryParams, Map<String, String> headerMap, Map<String, Object> bodyMap);
}
