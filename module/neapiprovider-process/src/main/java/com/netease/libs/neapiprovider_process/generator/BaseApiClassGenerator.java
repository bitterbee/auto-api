package com.netease.libs.neapiprovider_process.generator;

import com.netease.libs.neapiprovider_process.BaseClassGenerator;
import com.netease.libs.neapiprovider_process.NEApiProviderClass;

import javax.annotation.processing.Messager;
import javax.lang.model.element.TypeElement;

/**
 * Created by zyl06 on 2018/10/18.
 */

public abstract class BaseApiClassGenerator extends BaseClassGenerator {

    protected NEApiProviderClass mProviderClass;
    protected TypeElement mApiTarget;
    protected String mPkgName;

    public BaseApiClassGenerator(NEApiProviderClass providerClass, Messager messager, String packageName) {
        super(messager);
        mProviderClass = providerClass;
        mApiTarget = providerClass.clazz;
        mPkgName = packageName;
    }
}
