package com.netease.libs.apiservice_process.generator;

import com.netease.libs.apiservice_process.ApiServiceClass;
import com.netease.libs.apiservice_process.FileUtil;
import com.netease.libs.apiservice_process.Logger;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;

import javax.annotation.processing.Filer;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;

import static com.squareup.javapoet.TypeSpec.interfaceBuilder;
import static javax.lang.model.element.Modifier.PUBLIC;

/**
 * Created by zyl06 on 2018/10/27.
 */

public class ApiFactoryGenerator extends BaseApiClassGenerator {

    private ClassName mReturnType;
    private int mFactoryMethodCount = 0;

    public ApiFactoryGenerator(ApiServiceClass providerClass, String pkgName, ApiGenerator apiGenerator) {
        super(providerClass, pkgName);
        mReturnType = ClassName.get(apiGenerator.packageName(), apiGenerator.className());
    }

    @Override
    public String packageName() {
        return (mPkgName == null || mPkgName.isEmpty()) ?
                "com.netease.libs.api_service.factory" :
                mPkgName + ".api_service.factory";
    }

    @Override
    public String className() {
        return className("ApiFactory", "ApiFactory");
    }

    @Override
    public TypeSpec generate() {
        TypeSpec.Builder builder = interfaceBuilder(className())
                .addModifiers(PUBLIC);
        builder.addJavadoc(mApiTarget.getQualifiedName().toString() + " api Class's factory Interface\n");

        generate(builder);

        if (mFactoryMethodCount == 0) {
            addFactoryMethod(builder, null, "newInstance");
        }

        return builder.build();
    }

    @Override
    public void writeTo(JavaFile javaFile, Filer filer) throws IOException {
        FileUtil.writeTo(javaFile);
    }

    @Override
    protected void addMethod(TypeSpec.Builder builder, ExecutableElement e, String methodName) {

    }

    @Override
    protected void addFactoryMethod(TypeSpec.Builder builder, ExecutableElement e, String methodName) {

        mFactoryMethodCount ++;

        MethodSpec.Builder methodBuilder = MethodSpec
                .methodBuilder(methodName)
                .returns(mReturnType)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT);
        if (e != null) {
            for (VariableElement param : e.getParameters()) {
                methodBuilder.addParameter(TypeName.get(param.asType()), param.getSimpleName().toString());
            }
        }

        builder.addMethod(methodBuilder.build());
    }

    @Override
    protected void addClassBuildMethod(TypeSpec.Builder builder, ExecutableElement e, String methodName) {
        TypeName returnType = ClassName.get(e.getReturnType());
        if (!returnType.equals(ClassName.get(mApiTarget))) {
            Logger.e(mApiTarget.toString() + "." + methodName + " return type is not " + mApiTarget.toString());
            return;
        }

        mFactoryMethodCount ++;

        MethodSpec.Builder methodBuilder = MethodSpec
                .methodBuilder(methodName)
                .returns(mReturnType)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT);
        if (e != null) {
            for (VariableElement param : e.getParameters()) {
                methodBuilder.addParameter(TypeName.get(param.asType()), param.getSimpleName().toString());
            }
        }

        builder.addMethod(methodBuilder.build());

    }
}
