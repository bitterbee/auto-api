package com.netease.demo.autoapi.auto_api.factory;

import com.netease.demo.autoapi.auto_api.DataModelApi;

/**
 * com.netease.demo.autoapi.DataModel api Class's factory Interface
 */
public interface DataModelApiFactory {
  DataModelApi newInstance(int a, int b);
}
