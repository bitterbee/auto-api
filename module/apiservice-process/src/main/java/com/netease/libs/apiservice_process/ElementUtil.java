package com.netease.libs.apiservice_process;

import com.netease.libs.apiservice.anno.ApiServiceClassAnno;
import com.netease.libs.apiservice_process.generator.ApiGenerator;
import com.netease.libs.apiservice_process.generator.StubClassGenerator;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

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
    static final Map<TypeName, ApiGenerator> API_GENERATORS = new HashMap<>();
    static final Map<TypeName, StubClassGenerator> STUB_GENERATORS = new HashMap<>();
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
            ApiGenerator apiGen = API_GENERATORS.get(className);
            return apiGen != null ? ClassName.get(apiGen.packageName(), apiGen.className()) : null;
        }

        if (e instanceof VariableElement) {
            VariableElement varElem = (VariableElement) e;
            ApiGenerator apiGen = API_GENERATORS.get(ClassName.get(varElem.asType()));
            return apiGen != null ? ClassName.get(apiGen.packageName(), apiGen.className()) : null;
        }

        return null;
    }

    public static TypeName getApiServiceClassName(TypeMirror type) {
        ApiGenerator apiGen = API_GENERATORS.get(ClassName.get(type));
        return apiGen != null ? ClassName.get(apiGen.packageName(), apiGen.className()) : null;
    }

    public static TypeName getStubServiceClassName(TypeMirror type) {
        StubClassGenerator apiGen = STUB_GENERATORS.get(ClassName.get(type));
        return apiGen != null ? ClassName.get(apiGen.packageName(), apiGen.className()) : null;
    }
}
