package com.netease.demo.apiservice.api_service;

import com.netease.libs.api_service.ApiBase;
import java.lang.Class;
import java.lang.String;
import java.util.List;

/**
 * com.netease.demo.apiservice.http.HttpUtil's api Interface
 */
public interface HttpUtilApi extends ApiBase {
  RequestApi query(HttpListenerApi listener);

  RequestApi queryNoCallback(HttpListenerApi listener);

  <T> T get(Class<T> aaaaaa);

  <T> List<T> toJsonArr(String jsonStr, Class<T> clazz);
}
