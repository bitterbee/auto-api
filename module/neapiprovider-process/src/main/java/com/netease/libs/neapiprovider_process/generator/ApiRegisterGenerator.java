package com.netease.libs.neapiprovider_process.generator;

import com.netease.libs.neapiprovider.ApiProvider;
import com.netease.libs.neapiprovider_process.BaseClassGenerator;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Modifier;

import static com.squareup.javapoet.TypeSpec.classBuilder;
import static javax.lang.model.element.Modifier.PUBLIC;

/**
 * Created by zyl06 on 2018/10/19.
 */

public class ApiRegisterGenerator extends BaseClassGenerator {

    private String mPkgName;
    private List<ApiStubClassGenerator> mApiStubsGenerators = new ArrayList<>();

    public ApiRegisterGenerator(Messager messager,
                                String pkgName,
                                List<ApiStubClassGenerator> apiStubClassGenerators) {
        super(messager);
        mPkgName = pkgName;
        mApiStubsGenerators = apiStubClassGenerators;
    }

    @Override
    public String packageName() {
        return mPkgName + ".api_provider";
    }

    @Override
    public String className() {
        return "ApiRegister";
    }

    @Override
    public TypeSpec generate() {
        TypeSpec.Builder builder = classBuilder(className())
                .addModifiers(PUBLIC);
        builder.addJavadoc("ApiProvider 注册类\n");

        MethodSpec.Builder initMethod = MethodSpec.methodBuilder("init")
                .addModifiers(Modifier.STATIC, Modifier.PUBLIC)
                .addJavadoc("集成模块的 ApiProvider 初始化方法\n")
                .returns(TypeName.VOID);
        for (ApiStubClassGenerator apiStub : mApiStubsGenerators) {
            ClassName apiStubClass = ClassName.get(apiStub.packageName(), apiStub.className());

            initMethod.addStatement("$T.APIS.put($S, new $T())", ApiProvider.class,
                    apiStub.mApiGenerator.className(), apiStubClass);
        }
        builder.addMethod(initMethod.build());

        return builder.build();
    }
}
