package com.netease.demo.autoapi.auto_api.factory;

import com.netease.demo.autoapi.auto_api.MinusApi;

/**
 * com.netease.demo.autoapi.Minus api Class's factory Interface
 */
public interface MinusApiFactory {
  MinusApi newInstance(int data1, int data2);
}
