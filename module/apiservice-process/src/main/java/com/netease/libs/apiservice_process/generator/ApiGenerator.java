package com.netease.libs.apiservice_process.generator;

import com.netease.libs.apiservice_process.ApiServiceClass;
import com.netease.libs.apiservice_process.FileUtil;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.File;
import java.io.IOException;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import static com.squareup.javapoet.TypeSpec.interfaceBuilder;
import static javax.lang.model.element.Modifier.PUBLIC;

/**
 * Created by zyl06 on 2018/10/17.
 */

public class ApiGenerator extends BaseApiClassGenerator {

    public ApiGenerator(ApiServiceClass providerClass, Messager messager, String pkgName) {
        super(providerClass, messager, pkgName);
    }

    @Override
    public String packageName() {
        return (mPkgName == null || mPkgName.isEmpty()) ?
                "com.netease.libs.api_service" :
                mPkgName + ".api_service";
    }

    @Override
    public String className() {
        return mProviderClass.name != null && !mProviderClass.name.equals("") ?
                mProviderClass.name :
                mApiTarget.getSimpleName() + "Api";
    }

    @Override
    public TypeSpec generate() {
        TypeSpec.Builder builder = interfaceBuilder(className())
                .addSuperinterface(ApiBaseGenerator.getClassName())
                .addModifiers(PUBLIC);
        builder.addJavadoc(mApiTarget.getQualifiedName().toString() + " 的 api 接口\n");

        generate(builder);

        return builder.build();
    }

    @Override
    public void writeTo(JavaFile javaFile, Filer filer) throws IOException {
        FileUtil.writeTo(javaFile, mMessager);
    }

    @Override
    protected void addMethod(TypeSpec.Builder builder, ExecutableElement e, String methodName) {
        MethodSpec.Builder methodBuilder = MethodSpec
                .methodBuilder(methodName)
                .returns(tryConvertApiTypeName(e.getReturnType()));
        for (VariableElement param : e.getParameters()) {
            methodBuilder.addParameter(tryConvertApiTypeName(param),
                    param.getSimpleName().toString());
        }
        for (TypeMirror throwType : e.getThrownTypes()) {
            methodBuilder.addException(TypeName.get(throwType));
        }
        methodBuilder.addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT);

        builder.addMethod(methodBuilder.build());
    }
}
