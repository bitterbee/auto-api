package com.netease.libs.apiservice_process;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

/**
 * Created by zyl06 on 2018/10/18.
 */

public class ElementUtil {
    public static boolean isPublic(Element element) {
        return element.getModifiers().contains(Modifier.PUBLIC);
    }
    public static boolean isAbstract(Element element) {
        return element.getModifiers().contains(Modifier.ABSTRACT);
    }
    public static boolean isStatic(Element element) {
        return element.getModifiers().contains(Modifier.STATIC);
    }
}
