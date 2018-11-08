package com.netease.demo.apiservice.test;

import com.netease.libs.apiservice.anno.ApiServiceClassAnno;
import com.netease.libs.apiservice.anno.ApiServiceClassBuildMethodAnno;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zyl06 on 2018/11/8.
 */

@ApiServiceClassAnno(includeSuperApi = true)
public class SharedWzpCommonRequestTask extends BaseWzpCommonRequestTask {

    private String mUrl;
    private String mApi;
    private Class mModelClass;

    private SharedWzpCommonRequestTask(SharedWzpDataModel data) {
        mUrl = data.mUrl;
        mApi = data.mApi;
        mModelClass = data.mModelClass;
    }

    public static class SharedWzpDataModel {
        private String mUrl;
        private String mApi;
        private Class mModelClass;
        private Map<String, String> mHeaderMap = new HashMap<>();
        private Map<String, Object> mBodyMap = new HashMap<>();
        private Map<String, String> mQueryParamsMap = new HashMap<>();

        private int mAppId = 11;
        private int mServiceId = 137;
    }

//    @ApiServiceMethodAnno()
//    public SharedRequest query(@ApiServiceCallbackAnno SharedHttpRequestListener listener) {
//        Request request = super.query(listener);
//        return new SharedRequest(request);
//    }

//    @ApiServiceMethodAnno()
//    public SharedRequest query(@ApiServiceCallbackAnno SharedHttpRequestListener listener,
//                               @ApiServiceCallbackAnno ISharedJsonParser jsonParser) {
//        Request request = super.query(listener, jsonParser);
//        return new SharedRequest(request);
//    }

//    @ApiServiceMethodAnno()
//    public SharedRequest queryArray(@ApiServiceCallbackAnno SharedHttpRequestListener listener) {
//        Request request = super.queryArray(listener);
//        return new SharedRequest(request);
//    }

//    @ApiServiceMethodAnno()
//    public SharedRequest queryArray(@ApiServiceCallbackAnno SharedHttpRequestListener listener,
//                                    @ApiServiceCallbackAnno ISharedJsonParser jsonParser) {
//        Request request = super.queryArray(listener, jsonParser);
//        return new SharedRequest(request);
//    }

    @ApiServiceClassBuildMethodAnno()
    public static SharedWzpCommonRequestTask newInstance(String url, Class modelClass,
                                                         Map<String, String> queryParams) {
        return newInstance(url, modelClass, queryParams, null, null);
    }

    @ApiServiceClassBuildMethodAnno()
    public static SharedWzpCommonRequestTask newInstance(String url, Class modelClass,
                                                         Map<String, String> queryParams,
                                                         Map<String, String> headerMap,
                                                         Map<String, Object> bodyMap) {
        return newInstance(url, modelClass, queryParams, headerMap, bodyMap, 0,
                11, 137);
    }

    @ApiServiceClassBuildMethodAnno()
    public static SharedWzpCommonRequestTask newInstance(String url, Class modelClass,
                                                         Map<String, String> queryParams,
                                                         Map<String, String> headerMap,
                                                         Map<String, Object> bodyMap,
                                                         int method,
                                                         int appId, int serviceId) {
        Builder builder = new Builder();
        return builder.setApi(url)
                .setModelClass(modelClass)
                .setQueryParams(queryParams)
                .setHeaders(headerMap)
                .setBodyMap(bodyMap)
                .setAppId(appId)
                .setServiceId(serviceId)
                .build();
    }

    public static class Builder {

        private SharedWzpDataModel mData = new SharedWzpDataModel();

        public Builder setUrl(String url) {
            mData.mUrl = url;
            return this;
        }

        public Builder setApi(String api) {
            mData.mApi = api;
            return this;
        }

        public Builder setModelClass(Class clazz) {
            mData.mModelClass = clazz;
            return this;
        }

        public Builder setHeaders(Map<String, String> headerMap) {
            if (headerMap != null) {
                mData.mHeaderMap = headerMap;
            }
            return this;
        }

        public Builder setBodyMap(Map<String, Object> bodyMap) {
            if (bodyMap != null) {
                mData.mBodyMap = bodyMap;
            }
            return this;
        }

        public Builder setQueryParams(Map<String, String> queryParamsMap) {
            if (queryParamsMap != null) {
                mData.mQueryParamsMap = queryParamsMap;
            }
            return this;
        }

        public Builder setAppId(int appId) {
            mData.mAppId = appId;
            return this;
        }

        public Builder setServiceId(int serviceId) {
            mData.mServiceId = serviceId;
            return this;
        }

        public SharedWzpCommonRequestTask build() {
            return new SharedWzpCommonRequestTask(mData);
        }
    }

}