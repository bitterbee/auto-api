package com.netease.demo.apiservice.api_service.factory;

import com.netease.demo.apiservice.api_service.SharedWzpCommonRequestTaskApi;
import java.lang.Class;
import java.lang.Object;
import java.lang.String;
import java.util.Map;

/**
 * com.netease.demo.apiservice.test.SharedWzpCommonRequestTask api 类的工厂接口
 */
public interface SharedWzpCommonRequestTaskApiFactory {
  SharedWzpCommonRequestTaskApi newInstance(String url, Class modelClass, Map<String, String> queryParams);

  SharedWzpCommonRequestTaskApi newInstance(String url, Class modelClass, Map<String, String> queryParams, Map<String, String> headerMap, Map<String, Object> bodyMap);

  SharedWzpCommonRequestTaskApi newInstance(String url, Class modelClass, Map<String, String> queryParams, Map<String, String> headerMap, Map<String, Object> bodyMap, int method, int appId, int serviceId);
}
