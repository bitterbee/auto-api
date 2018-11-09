package com.netease.libs.apiservice_process.generator;

import com.netease.libs.apiservice_process.ApiServiceClass;
import com.netease.libs.apiservice_process.FileUtil;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
import com.sun.tools.javac.code.Type;

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

    public ApiGenerator(ApiServiceClass providerClass, String pkgName) {
        super(providerClass, pkgName);
    }

    @Override
    public String packageName() {
        return (mPkgName == null || mPkgName.isEmpty()) ?
                "com.netease.libs.api_service" :
                mPkgName + ".api_service";
    }

    @Override
    public String className() {
        return className(null, "Api");
    }

    @Override
    public TypeSpec generate() {
        TypeSpec.Builder builder = interfaceBuilder(className())
                .addSuperinterface(ApiBaseGenerator.getClassName())
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

        if (e.getReturnType() instanceof Type.TypeVar) {
            Type.TypeVar typeVar = (Type.TypeVar) e.getReturnType();
            methodBuilder.addTypeVariable(TypeVariableName.get(typeVar));
        }

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
