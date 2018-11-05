package com.netease.libs.apiservice_process.generator;

import com.netease.libs.apiservice.anno.ApiServiceClassBuildMethodAnno;
import com.netease.libs.apiservice.anno.ApiServiceConstructAnno;
import com.netease.libs.apiservice.anno.ApiServiceMethodAnno;
import com.netease.libs.apiservice_process.ApiServiceClass;
import com.netease.libs.apiservice_process.BaseClassGenerator;
import com.netease.libs.apiservice_process.ElementUtil;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

/**
 * Created by zyl06 on 2018/10/18.
 */

public abstract class BaseApiClassGenerator extends BaseClassGenerator {

    protected ApiServiceClass mProviderClass;
    protected TypeElement mApiTarget;
    protected String mPkgName;
    private Set<String> mMethodSignatures = new HashSet<>();

    public BaseApiClassGenerator(ApiServiceClass providerClass, Messager messager, String packageName) {
        super(messager);
        mProviderClass = providerClass;
        mApiTarget = providerClass.clazz;
        mPkgName = packageName;
    }

    private boolean addSignature(ExecutableElement e) {
        StringBuilder sb = new StringBuilder(64);
        sb.append(e.getSimpleName()).append("_");
        for (VariableElement ve : e.getParameters()) {
            sb.append(ClassName.get(ve.asType()).toString())
                    .append("_");
        }
        return mMethodSignatures.add(sb.toString());
    }

    protected void generate(TypeSpec.Builder builder) {
        for (Element e : mApiTarget.getEnclosedElements()) {
            if (!ElementUtil.isPublic(e)) {
                continue;
            }
            if (e instanceof ExecutableElement) {
                ApiServiceConstructAnno constructAnno = e.getAnnotation(ApiServiceConstructAnno.class);
                if (constructAnno != null) {
                    String methodName = !constructAnno.alias().isEmpty() ? constructAnno.alias() : "newInstance";
                    addFactoryMethod(builder, (ExecutableElement) e, methodName);
                    continue;
                }

                ApiServiceClassBuildMethodAnno classBuildAnno = e.getAnnotation(ApiServiceClassBuildMethodAnno.class);
                if (classBuildAnno != null) {
                    if (ElementUtil.isPublic(e) && ElementUtil.isStatic(e)) {
                        String methodName = !classBuildAnno.alias().isEmpty() ? constructAnno.alias() : e.getSimpleName().toString();
                        addClassBuildMethod(builder, (ExecutableElement) e, methodName);
                    }
                    continue;
                }

                runExeElement(builder, (ExecutableElement) e);
            }
        }

        // 处理基类
        if (mProviderClass.includeSuperApi) {
            TypeMirror tm = mApiTarget.getSuperclass();
            while (tm instanceof TypeElement) {
                TypeElement superElem = (TypeElement) tm;
                ClassName cn = ClassName.get(superElem);
                if (cn.isPrimitive() || cn.equals(ClassName.OBJECT)) {
                    break;
                }

                for (Element e : mApiTarget.getEnclosedElements()) {
                    if (ElementUtil.isPublic(e) && e instanceof ExecutableElement) {
                        runExeElement(builder, (ExecutableElement) e);
                    }
                }
            }

            if (ElementUtil.isInterface(mApiTarget)) {
                for (TypeMirror funcInter : mApiTarget.getInterfaces()) {
                    mMessager.printMessage(Diagnostic.Kind.WARNING, "funcInter = " + funcInter);
                    Element funcTypeElem = ((DeclaredType) funcInter).asElement();

                    for (Element e : funcTypeElem.getEnclosedElements()) {
                        if (e instanceof ExecutableElement && ElementUtil.isPublic(e)) {
                            runExeElement(builder, (ExecutableElement) e);
                        }
                    }
                }
            }

        }
    }

    private void runExeElement(TypeSpec.Builder builder, ExecutableElement e) {
        if (!addSignature(e)) {
            return;
        }

        ApiServiceMethodAnno anno = e.getAnnotation(ApiServiceMethodAnno.class);
        if (anno != null && !anno.provide()) {
            return;
        }

        if (e.getKind() == ElementKind.CONSTRUCTOR) {
            return;
        }

        boolean provide = (anno != null && anno.provide()) ||
                (mProviderClass.allPublicStaticApi && ElementUtil.isStatic(e)) ||
                (mProviderClass.allPublicNormalApi && !ElementUtil.isStatic(e));
        if (provide) {
            String methodName = anno != null && !anno.alias().isEmpty() ?
                    anno.alias() : e.getSimpleName().toString();
            addMethod(builder, e, methodName);
        }
    }

    protected void addCustomFields(TypeSpec.Builder builder) {

    }

    protected void addMethod(TypeSpec.Builder builder, ExecutableElement e, String methodName) {

    }

    protected void addFactoryMethod(TypeSpec.Builder builder, ExecutableElement e, String methodName) {

    }

    protected void addClassBuildMethod(TypeSpec.Builder builder, ExecutableElement e, String methodName) {

    }

    protected String className(String providerNameSuffix, String anonymousSuffix) {
        if (mProviderClass.name != null && !mProviderClass.name.equals("")) {
            return providerNameSuffix != null ?
                    mProviderClass.name + providerNameSuffix :
                    mProviderClass.name;
        }

        List<String> enclosingNames = new LinkedList<>();
        Element enclosingElem = mApiTarget.getEnclosingElement();
        while (enclosingElem instanceof TypeElement) {
            enclosingNames.add(0, enclosingElem.getSimpleName().toString());
            enclosingElem = enclosingElem.getEnclosingElement();
        }

        StringBuilder sb = new StringBuilder();
        for (String enclosingName : enclosingNames) {
            sb.append(enclosingName).append("_");
        }
        sb.append(mApiTarget.getSimpleName());
        if (anonymousSuffix != null) {
            sb.append(anonymousSuffix);
        }

        return sb.toString();
    }

    /**
     *
     * @param e 具体真实 Element
     * @return 返回 对应 Api 类 TypeName 或元素自身的 TypeName
     */
    public TypeName tryConvertApiTypeName(Element e) {
        TypeName apiTypeName = ElementUtil.getApiServiceClassName(e);
        return apiTypeName != null ? apiTypeName : ClassName.get(e.asType());
    }

    /**
     *
     * @param type 真实类型
     * @return 返回 对应 Api 类 TypeName 或元素自身的 TypeName
     */
    public TypeName tryConvertApiTypeName(TypeMirror type) {
        TypeName apiTypeName = ElementUtil.getApiServiceClassName(type);
        return apiTypeName != null ? apiTypeName : ClassName.get(type);
    }
}
