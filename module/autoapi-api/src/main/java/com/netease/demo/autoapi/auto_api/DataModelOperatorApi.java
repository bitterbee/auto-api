package com.netease.demo.autoapi.auto_api;

import com.netease.libs.auto_api.ApiBase;

/**
 * com.netease.demo.autoapi.DataModelOperator's api Interface
 */
public interface DataModelOperatorApi extends ApiBase {
  DataModelApi add(DataModelApi a, DataModelApi b);
}
