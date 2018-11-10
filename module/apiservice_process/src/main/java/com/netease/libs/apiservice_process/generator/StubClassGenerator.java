package com.netease.libs.apiservice_process.generator;

import com.netease.libs.apiservice.anno.ApiServiceCallbackAnno;
import com.netease.libs.apiservice_process.ApiServiceClass;
import com.netease.libs.apiservice_process.ElementUtil;
import com.netease.libs.apiservice_process.Logger;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
import com.sun.tools.javac.code.Type;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import static com.squareup.javapoet.TypeSpec.classBuilder;
import static javax.lang.model.element.Modifier.PUBLIC;

/**
 * Created by zyl06 on 2018/10/18.
 */

public class StubClassGenerator extends BaseApiClassGenerator {

    private ClassName mApiClassName;
    private boolean mMakeTargetField = false;
    private static final String TARGET_FILED_NAME = "mTarget";

    public StubClassGenerator(ApiServiceClass providerClass,
                              ApiGenerator apiGen,
                              String packageName) {
        super(providerClass, packageName);
        this.mApiClassName = ClassName.get(apiGen.packageName(), apiGen.className());
    }

    @Override
    public String packageName() {
        return (mPkgName == null || mPkgName.isEmpty()) ?
                "com.netease.libs.api_service" :
                mPkgName + ".api_service";
    }

    @Override
    public String className() {
        return className("Stub", "Stub");
    }

    @Override
    public TypeSpec generate() {

        TypeSpec.Builder builder = classBuilder(className())
                .addSuperinterface(mApiClassName)
                .addModifiers(PUBLIC);
        builder.addJavadoc(mApiTarget.getQualifiedName().toString() + "'s stub Class\n");

        generate(builder);

        addCustomFields(builder);
        addCustomMethods(builder);

        return builder.build();
    }

    protected void addCustomMethods(TypeSpec.Builder builder) {
        MethodSpec.Builder methodBuilder = MethodSpec
                .methodBuilder(ApiBaseGenerator.GET_TARGET_METHOD_NAME)
                .returns(TypeName.OBJECT)
                .addModifiers(Modifier.PUBLIC);
        methodBuilder.addStatement(mMakeTargetField ?
                "return " + TARGET_FILED_NAME :
                "return null");
        builder.addMethod(methodBuilder.build());
    }

    protected void addCustomFields(TypeSpec.Builder builder) {
        // 生成成员变量
        if (mMakeTargetField) {
            FieldSpec.Builder targetBuild = FieldSpec.builder(TypeName.get(mApiTarget.asType()),
                    TARGET_FILED_NAME, Modifier.PUBLIC);
            FieldSpec target = targetBuild.addJavadoc("non-static function's owner Object\n").build();
            builder.addField(target);
        }
    }

    @Override
    protected void addMethod(TypeSpec.Builder builder, ExecutableElement e, String methodName) {
        Logger.w("return is " + e.getReturnType() + "; type is " + e.getReturnType().getClass());

        TypeName returnApiType = ElementUtil.getApiServiceClassName(e.getReturnType());
        boolean hasReturn = e.getReturnType() != null && TypeName.get(e.getReturnType()) != TypeName.VOID;
        boolean isStatic = ElementUtil.isStatic(e);

        MethodSpec.Builder methodBuilder = MethodSpec
                .methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC)
                // 如果是自定义类型，需要修改成 api 类
                .returns(returnApiType != null ? returnApiType : ClassName.get(e.getReturnType()));

        addTemplateTypes(methodBuilder, e.getReturnType());

        for (TypeMirror throwType : e.getThrownTypes()) {
            methodBuilder.addException(TypeName.get(throwType));
        }

        StringBuilder sb = new StringBuilder(32);
        if (hasReturn) {
            if (returnApiType != null) {
                TypeName returnStubType = ElementUtil.getStubServiceClassName(e.getReturnType());
                methodBuilder.addStatement("$T stub = new $T()", returnStubType, returnStubType);
                sb.append(String.format("stub.%s = ", TARGET_FILED_NAME));
            } else {
                sb.append("return ");
            }
        }
        if (isStatic) { // 静态方法
            sb.append("$T.")
                    .append(e.getSimpleName().toString())
                    .append("(");
        } else { // 非静态方法
            mMakeTargetField = true;

            sb.append(TARGET_FILED_NAME)
                    .append(".")
                    .append(e.getSimpleName().toString())
                    .append("(");
        }

        List<Object> statementArgs = new ArrayList<>();
        if (ElementUtil.isStatic(e)) { // 静态方法
            statementArgs.add(mApiTarget);
        }

        List<? extends VariableElement> params = e.getParameters();
        int paramCount = params.size();
        for (int i = 0; i < paramCount; i++) {
            VariableElement param = params.get(i);

            TypeName apiTypeName = ElementUtil.getApiServiceClassName(param);
            if (apiTypeName != null) {
                methodBuilder.addParameter(apiTypeName,
                        param.getSimpleName().toString());

                ApiServiceCallbackAnno callbackAnno = param.getAnnotation(ApiServiceCallbackAnno.class);
                if (callbackAnno != null) {
                    // callback，组装成 ApiCallback 对象
                    TypeName callbackType = ElementUtil.getCallbackClassName(param.asType());
                    Logger.w("callbackType = " + callbackType + "; param.asType() = " + param.asType());
                    methodBuilder.addStatement("$T callback = new $T()", callbackType, callbackType);
                    methodBuilder.addStatement(String.format("callback.%s = %s", TARGET_FILED_NAME, param.getSimpleName()));

                    sb.append("callback");
                } else {
                    // 非 callback，通过 getApiServiceTarget 获取真实对象
                    statementArgs.add(ClassName.get(param.asType()));
                    sb.append("($T)")
                            .append(param.getSimpleName().toString())
                            .append(".")
                            .append(ApiBaseGenerator.GET_TARGET_METHOD_NAME)
                            .append("()");
                }

            } else {
                methodBuilder.addParameter(ClassName.get(param.asType()), param.getSimpleName().toString());
                sb.append(param.getSimpleName().toString());
            }

            if (i < paramCount - 1) {
                sb.append(",");
            }
        }
        sb.append(")");

        if (statementArgs.isEmpty()) {
            methodBuilder.addStatement(sb.toString());
        } else {
            methodBuilder.addStatement(sb.toString(), statementArgs.toArray());
        }

        // 如果是自定义类型，需要重新添加return，将 api 类返回
        if (returnApiType != null) {
            methodBuilder.addStatement("return stub");
        }

        builder.addMethod(methodBuilder.build());
    }

    @Override
    protected void addFactoryMethod(TypeSpec.Builder builder, ExecutableElement e, String methodName) {
        mMakeTargetField = true;
    }
}
