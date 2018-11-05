package com.netease.demo.apiservice.api_service.factory;

import com.netease.demo.apiservice.api_service.RequestApi;

/**
 * com.netease.demo.apiservice.http.Request api 类的工厂接口
 */
public interface RequestApiFactory {
  RequestApi newInstance();
}
