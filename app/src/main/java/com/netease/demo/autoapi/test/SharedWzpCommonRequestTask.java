package com.netease.demo.autoapi.test;

import com.netease.libs.autoapi.anno.AutoApiClassAnno;
import com.netease.libs.autoapi.anno.AutoApiClassBuildMethodAnno;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zyl06 on 2018/11/8.
 */

@AutoApiClassAnno(includeSuperApi = true)
public class SharedWzpCommonRequestTask extends BaseWzpCommonRequestTask {

    private String mUrl;
    private String mApi;
    private Class mModelClass;

    @AutoApiClassBuildMethodAnno()
    public static SharedWzpCommonRequestTask newInstance(String url, Class modelClass,
                                                         Map<String, String> queryParams,
                                                         Map<String, String> headerMap,
                                                         Map<String, Object> bodyMap) {
        Builder builder = new Builder();
        return builder.setApi(url)
                .setModelClass(modelClass)
                .setQueryParams(queryParams)
                .setHeaders(headerMap)
                .setBodyMap(bodyMap)
                .build();
    }

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