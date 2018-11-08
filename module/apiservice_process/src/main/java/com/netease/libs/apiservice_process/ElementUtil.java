package com.netease.libs.apiservice_process;

import com.netease.libs.apiservice.anno.ApiServiceClassAnno;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

/**
 * Created by zyl06 on 2018/10/18.
 */

public class ElementUtil {
    static final Map<TypeName, TypeName> ORIGIN_TO_API = new HashMap<>();
    static final Map<TypeName, TypeName> ORIGIAN_TO_STUB = new HashMap<>();
    static final Map<TypeName, TypeName> ORIGIN_TO_CALLBACK = new HashMap<>();

    static Elements sElementUtil;

    public static boolean isPublic(Element element) {
        return element.getModifiers().contains(Modifier.PUBLIC);
    }

    public static boolean isAbstract(Element element) {
        return element.getModifiers().contains(Modifier.ABSTRACT);
    }

    public static boolean isStatic(Element element) {
        return element.getModifiers().contains(Modifier.STATIC);
    }

    public static boolean isInterface(TypeElement elem) {
        return elem.getKind() == ElementKind.INTERFACE;
    }

    public static ClassName getClassName(BaseClassGenerator classGen) {
        return ClassName.get(classGen.packageName(), classGen.className());
    }

    /**
     *
     * @param e 具体 Element
     * @return 返回 对应 Api 类 TypeName
     */
    public static TypeName getApiServiceClassName(Element e) {
        if (e instanceof TypeElement) {
            ApiServiceClassAnno anno = e.getAnnotation(ApiServiceClassAnno.class);
            if (anno == null) {
                return null;
            }
            ClassName className = ClassName.get((TypeElement) e);
            return ORIGIN_TO_API.get(className);
        }

        if (e instanceof VariableElement) {
            VariableElement varElem = (VariableElement) e;
            return ORIGIN_TO_API.get(ClassName.get(varElem.asType()));
        }

        return null;
    }

    public static TypeName getApiServiceClassName(TypeMirror type) {
        return ORIGIN_TO_API.get(ClassName.get(type));
    }

    public static TypeName getStubServiceClassName(TypeMirror type) {
        return ORIGIAN_TO_STUB.get(ClassName.get(type));
    }

    public static TypeName getCallbackClassName(TypeMirror type) {
        return ORIGIN_TO_CALLBACK.get(ClassName.get(type));
    }

    public static String defaultValue(TypeName tn) {
        if (!tn.isPrimitive()) {
            return "null";
        }

        if (tn.equals(TypeName.BOOLEAN)) {
            return "false";
        } else if (tn.equals(TypeName.BYTE) ||
                tn.equals(TypeName.SHORT) ||
                tn.equals(TypeName.INT) ||
                tn.equals(TypeName.LONG) ||
                tn.equals(TypeName.CHAR)) {
            return "0";
        } else if (tn.equals(TypeName.FLOAT) ||
                tn.equals(TypeName.DOUBLE)) {
            return "0.f";
        }
        return null;
    }
}
