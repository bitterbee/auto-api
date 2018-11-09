package com.netease.demo.apiservice.api_service.factory;

import com.netease.demo.apiservice.api_service.AppDataModel;

/**
 * com.netease.demo.apiservice.DataModel api Class's factory Interface
 */
public interface AppDataModelApiFactory {
  AppDataModel newInstance(int a, int b);
}
