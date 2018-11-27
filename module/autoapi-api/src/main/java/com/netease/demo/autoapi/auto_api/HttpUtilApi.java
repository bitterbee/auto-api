package com.netease.demo.autoapi.auto_api;

import com.netease.libs.auto_api.ApiBase;
import java.lang.Class;
import java.lang.String;
import java.util.List;

/**
 * com.netease.demo.autoapi.http.HttpUtil's api Interface
 */
public interface HttpUtilApi extends ApiBase {
  RequestApi query(HttpListenerApi listener);

  RequestApi queryNoCallback(HttpListenerApi listener);

  <T> T get(Class<T> aaaaaa);

  <T> List<T> toJsonArr(String jsonStr, Class<T> clazz);
}
