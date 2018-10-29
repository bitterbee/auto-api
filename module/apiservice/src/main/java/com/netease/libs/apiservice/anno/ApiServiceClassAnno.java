package com.netease.libs.apiservice.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zyl06 on 2018/10/16.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface ApiServiceClassAnno {
    /**
     * 接口提供者名字
     * @return
     */
    String name() default "";

    /**
     * 是否提供全部 public static 工具方法？
     * @return
     */
    boolean allPublicStaticApi() default false;
}
