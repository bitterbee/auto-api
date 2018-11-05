package com.netease.demo.apiservice.api_service.factory;

import com.netease.demo.apiservice.api_service.HttpUtilApi;

/**
 * com.netease.demo.apiservice.http.HttpUtil api 类的工厂接口
 */
public interface HttpUtilApiFactory {
  HttpUtilApi newInstance();
}
