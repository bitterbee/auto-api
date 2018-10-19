package com.netease.libs.neapiprovider_process;

import javax.annotation.processing.Messager;
import javax.lang.model.element.TypeElement;

/**
 * Created by zyl06 on 2018/10/18.
 */

public abstract class BaseApiClassGenerator extends BaseClassGenerator {

    protected NEApiProviderClass mProviderClass;
    protected TypeElement mApiTarget;

    public BaseApiClassGenerator(NEApiProviderClass providerClass, Messager messager) {
        super(messager);
        mProviderClass = providerClass;
        mApiTarget = providerClass.clazz;
    }
}
