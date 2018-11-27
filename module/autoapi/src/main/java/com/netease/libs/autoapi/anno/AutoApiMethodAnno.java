package com.netease.libs.autoapi.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zyl06 on 2018/10/26.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface AutoApiMethodAnno {
    /**
     * 是否暴露该 api
     * @return
     */
    boolean provide() default true;

    /**
     * 方法别名，如果为空则使用原方法名
     * @return
     */
    String alias() default "";


}
