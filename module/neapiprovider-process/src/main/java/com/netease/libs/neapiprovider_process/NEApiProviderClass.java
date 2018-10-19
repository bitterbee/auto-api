package com.netease.libs.neapiprovider_process;

import javax.lang.model.element.TypeElement;

/**
 * Created by zyl06 on 2018/10/17.
 */

public class NEApiProviderClass {
    // NEApiProviderAnno 注解对应的类型
    public TypeElement clazz;
    // api provider 名称
    public String name;
    // 是否提供全部的静态方法
    public boolean provideStaticApi;
    // 是否解析父类
    public boolean includeSuper;
}