package com.netease.libs.autoapi.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zyl06 on 2018/10/28.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface AutoApiClassBuildMethodAnno {
    String alias() default "";
}
