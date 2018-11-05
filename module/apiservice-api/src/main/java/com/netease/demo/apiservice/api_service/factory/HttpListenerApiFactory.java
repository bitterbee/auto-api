package com.netease.demo.apiservice.api_service.factory;

import com.netease.demo.apiservice.api_service.HttpListenerApi;

/**
 * com.netease.demo.apiservice.http.HttpListener api 类的工厂接口
 */
public interface HttpListenerApiFactory {
  HttpListenerApi newInstance();
}
