package com.netease.libs.autoapi_process.generator;

import com.netease.libs.autoapi_process.AutoApiClass;
import com.netease.libs.autoapi_process.FileUtil;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;

import javax.annotation.processing.Filer;
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

    public ApiGenerator(AutoApiClass providerClass, String pkgName) {
        super(providerClass, pkgName);
    }

    @Override
    public String packageName() {
        return (mPkgName == null || mPkgName.isEmpty()) ?
                "com.netease.libs.auto_api" :
                mPkgName + ".auto_api";
    }

    @Override
    public String className() {
        return className(null, "Api");
    }

    @Override
    public TypeSpec generate() {
        TypeSpec.Builder builder = interfaceBuilder(className())
                .addSuperinterface(com.netease.libs.autoapi_process.generator.ApiBaseGenerator.getClassName())
                .addModifiers(PUBLIC);
        builder.addJavadoc(mApiTarget.getQualifiedName().toString() + "'s api Interface\n");

        generate(builder);

        return builder.build();
    }

    @Override
    public void writeTo(JavaFile javaFile, Filer filer) throws IOException {
        FileUtil.writeTo(javaFile);
    }

    @Override
    protected void addMethod(TypeSpec.Builder builder, ExecutableElement e, String methodName) {
        MethodSpec.Builder methodBuilder = MethodSpec
                .methodBuilder(methodName)
                .returns(tryConvertApiTypeName(e.getReturnType()));
        addTemplateTypes(methodBuilder, e.getReturnType());

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
