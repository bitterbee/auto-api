package com.netease.libs.apiservice_process;

import javax.lang.model.element.TypeElement;

/**
 * Created by zyl06 on 2018/10/17.
 */

public class ApiServiceClass {
    // NEApiProviderAnno 注解对应的类型
    public TypeElement clazz;

    // api provider 名称
    public String name;

    // 是否提供全部的 public static 方法
    public boolean allPublicStaticApi;

    // 是否提供全部的 public normal 方法
    public boolean allPublicNormalApi;

    // 是否判断基类方法
    public boolean includeSuperApi;
}