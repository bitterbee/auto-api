package com.netease.libs.autoapi_process.generator;

import com.netease.libs.autoapi_process.AutoApiClass;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import static com.squareup.javapoet.TypeSpec.classBuilder;
import static javax.lang.model.element.Modifier.PUBLIC;

/**
 * Created by zyl06 on 2018/11/5.
 * 针对 interface
 */
public class CallbackClassGenerator extends BaseApiClassGenerator {

    private ClassName mApiClassName;
    private boolean mMakeTargetField = false;
    private static final String TARGET_FILED_NAME = "mTarget";

    public CallbackClassGenerator(AutoApiClass providerClass,
                                  ApiGenerator apiGen,
                                  String packageName) {
        super(providerClass, packageName);
        this.mApiClassName = ClassName.get(apiGen.packageName(), apiGen.className());
    }

    @Override
    public String packageName() {
        return (mPkgName == null || mPkgName.isEmpty()) ?
                "com.netease.libs.auto_api" :
                mPkgName + ".auto_api";
    }

    @Override
    public String className() {
        return className("Callback", "Callback");
    }

    @Override
    public TypeSpec generate() {

        TypeSpec.Builder builder = classBuilder(className())
                .addSuperinterface(TypeName.get(mApiTarget.asType()))
                .addModifiers(PUBLIC);
        builder.addJavadoc(mApiTarget.getQualifiedName().toString() + "'s Callback Class\n");

        generate(builder);

        addCustomFields(builder);
        addCustomMethods(builder);

        return builder.build();
    }

    protected void addCustomMethods(TypeSpec.Builder builder) {
        MethodSpec.Builder methodBuilder = MethodSpec
                .methodBuilder(com.netease.libs.autoapi_process.generator.ApiBaseGenerator.GET_TARGET_METHOD_NAME)
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
            FieldSpec.Builder targetBuild = FieldSpec.builder(mApiClassName,
                    TARGET_FILED_NAME, Modifier.PUBLIC);
            FieldSpec target = targetBuild.addJavadoc("Callback's original Object\n").build();
            builder.addField(target);
        }
    }

    @Override
    protected void addMethod(TypeSpec.Builder builder, ExecutableElement e, String methodName) {
        TypeName returnApiType = com.netease.libs.autoapi_process.ElementUtil.getApiServiceClassName(e.getReturnType());

        MethodSpec.Builder methodBuilder = MethodSpec
                .methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC)
                // 如果是自定义类型，需要修改成 api 类
                .returns(returnApiType != null ? returnApiType : ClassName.get(e.getReturnType()));

        for (TypeMirror throwType : e.getThrownTypes()) {
            methodBuilder.addException(TypeName.get(throwType));
        }

        StringBuilder sb = new StringBuilder(32);
        if (e.getReturnType() != null && TypeName.get(e.getReturnType()) != TypeName.VOID) {
            if (returnApiType != null) {
                TypeName returnStubType = com.netease.libs.autoapi_process.ElementUtil.getStubServiceClassName(e.getReturnType());
                methodBuilder.addStatement("$T stub = new $T()", returnStubType, returnStubType);
                sb.append(String.format("stub.%s = ", TARGET_FILED_NAME));
            } else {
                sb.append("return ");
            }
        }
        if (com.netease.libs.autoapi_process.ElementUtil.isStatic(e)) { // 静态方法
            sb.append("$T.")
                    .append(e.getSimpleName().toString())
                    .append("(");
        } else { // 非静态方法
            mMakeTargetField = true;

            sb.append(TARGET_FILED_NAME).append(".")
                    .append(e.getSimpleName().toString())
                    .append("(");
        }

        List<Object> statementArgs = new ArrayList<>();
        if (com.netease.libs.autoapi_process.ElementUtil.isStatic(e)) { // 静态方法
            statementArgs.add(mApiTarget);
        }

        List<? extends VariableElement> params = e.getParameters();
        int paramCount = params.size();
        for (int i = 0; i < paramCount; i++) {
            VariableElement param = params.get(i);

            TypeName apiTypeName = com.netease.libs.autoapi_process.ElementUtil.getApiServiceClassName(param);
            if (apiTypeName != null) {
                methodBuilder.addParameter(apiTypeName,
                        param.getSimpleName().toString());
                statementArgs.add(ClassName.get(param.asType()));

                sb.append("($T)")
                        .append(param.getSimpleName().toString())
                        .append(".")
                        .append(com.netease.libs.autoapi_process.generator.ApiBaseGenerator.GET_TARGET_METHOD_NAME)
                        .append("()");
            } else {
                methodBuilder.addParameter(ClassName.get(param.asType()), param.getSimpleName().toString());
                sb.append(param.getSimpleName().toString());
            }

            if (i < paramCount - 1) {
                sb.append(",");
            }
        }
        sb.append(")");

        if (mMakeTargetField) {
            methodBuilder.beginControlFlow(String.format("if (%s != null)", TARGET_FILED_NAME));
        }
        if (statementArgs.isEmpty()) {
            methodBuilder.addStatement(sb.toString());
        } else {
            methodBuilder.addStatement(sb.toString(), statementArgs.toArray());
        }
        if (mMakeTargetField) {
            methodBuilder.endControlFlow();
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