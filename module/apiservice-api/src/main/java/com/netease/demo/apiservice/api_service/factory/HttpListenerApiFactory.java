package com.netease.demo.apiservice.api_service.factory;

import com.netease.demo.apiservice.api_service.HttpListenerApi;

/**
 * com.netease.demo.apiservice.http.HttpListener api Class's factory Interface
 */
public interface HttpListenerApiFactory {
  HttpListenerApi newInstance();
}
