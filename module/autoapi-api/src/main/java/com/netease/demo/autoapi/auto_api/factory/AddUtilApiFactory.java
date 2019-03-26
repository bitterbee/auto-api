package com.netease.demo.autoapi.auto_api.factory;

import com.netease.demo.autoapi.auto_api.AddUtilApi;

/**
 * com.netease.demo.autoapi.AddUtil api Class's factory Interface
 */
public interface AddUtilApiFactory {
  AddUtilApi newInstance(int data1, int data2);

  AddUtilApi newInstance(int data);

  AddUtilApi getInstance(int data1, int data2);
}
