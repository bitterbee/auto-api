package com.netease.demo.autoapi.auto_api.factory;

import com.netease.demo.autoapi.auto_api.HttpListenerApi;

/**
 * com.netease.demo.autoapi.http.HttpListener api Class's factory Interface
 */
public interface HttpListenerApiFactory {
  HttpListenerApi newInstance();
}
