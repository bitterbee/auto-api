package com.netease.libs.apiservice_process.generator;

import com.netease.libs.apiservice_process.BaseClassGenerator;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.File;
import java.io.IOException;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Modifier;

import static com.squareup.javapoet.TypeSpec.interfaceBuilder;
import static javax.lang.model.element.Modifier.PUBLIC;

/**
 * Created by zyl06 on 2018/10/29.
 */

public class ApiBaseGenerator extends BaseClassGenerator {

    private static final String PACKNAME = "com.netease.libs.api_service";
    private static final String CLASS_NAME = "ApiBase";
    public static final String GET_TARGET_METHOD_NAME = "getApiServiceTarget";

    private String mToPath;

    public ApiBaseGenerator(Messager messager, String apiProjectPath) {
        super(messager);
        mToPath = apiProjectPath;
    }

    @Override
    public String packageName() {
        return PACKNAME;
    }

    @Override
    public String className() {
        return CLASS_NAME;
    }

    @Override
    public TypeSpec generate() {
        TypeSpec.Builder builder = interfaceBuilder(className())
                .addModifiers(PUBLIC);
        builder.addJavadoc("api service 接口的父接口\n");

        MethodSpec.Builder methodBuilder = MethodSpec
                .methodBuilder(GET_TARGET_METHOD_NAME)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(TypeName.OBJECT);
        builder.addMethod(methodBuilder.build());

        return builder.build();
    }

    @Override
    public void writeTo(JavaFile javaFile, Filer filer) throws IOException {
        String to = mToPath + "/src/main/java/";
        File toDir = new File(to);
        if (!toDir.exists()) {
            boolean success = toDir.mkdirs();
            if (!success) {
                printError(to + " folder not exists!!!");
                return;
            }
        }
        javaFile.writeTo(toDir);
    }

    public static ClassName getClassName() {
        return ClassName.get(PACKNAME, CLASS_NAME);
    }
}
