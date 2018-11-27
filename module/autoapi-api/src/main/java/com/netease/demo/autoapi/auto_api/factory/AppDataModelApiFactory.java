package com.netease.demo.autoapi.auto_api.factory;

import com.netease.demo.autoapi.auto_api.AppDataModel;

/**
 * com.netease.demo.autoapi.DataModel api Class's factory Interface
 */
public interface AppDataModelApiFactory {
  AppDataModel newInstance(int a, int b);
}
