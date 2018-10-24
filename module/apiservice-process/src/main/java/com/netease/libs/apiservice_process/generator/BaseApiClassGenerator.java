package com.netease.libs.apiservice_process.generator;

import com.netease.libs.apiservice_process.BaseClassGenerator;
import com.netease.libs.apiservice_process.ApiServiceClass;

import javax.annotation.processing.Messager;
import javax.lang.model.element.TypeElement;

/**
 * Created by zyl06 on 2018/10/18.
 */

public abstract class BaseApiClassGenerator extends BaseClassGenerator {

    protected ApiServiceClass mProviderClass;
    protected TypeElement mApiTarget;
    protected String mPkgName;

    public BaseApiClassGenerator(ApiServiceClass providerClass, Messager messager, String packageName) {
        super(messager);
        mProviderClass = providerClass;
        mApiTarget = providerClass.clazz;
        mPkgName = packageName;
    }
}
