package com.netease.libs.apiservice_process.generator;

import com.netease.libs.apiservice.ApiService;
import com.netease.libs.apiservice_process.BaseClassGenerator;
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
    private List<StubFactoryGenerator> mStubFactoryGenerators = new ArrayList<>();

    public ApiRegisterGenerator(Messager messager,
                                String pkgName,
                                List<StubFactoryGenerator> stubFactoryGenerators) {
        super(messager);
        mPkgName = pkgName;
        mStubFactoryGenerators = stubFactoryGenerators;
    }

    @Override
    public String packageName() {
        return mPkgName + ".api_service";
    }

    @Override
    public String className() {
        return "ApiRegister";
    }

    @Override
    public TypeSpec generate() {
        TypeSpec.Builder builder = classBuilder(className())
                .addModifiers(PUBLIC);
        builder.addJavadoc("ApiService 注册类\n");

        MethodSpec.Builder initMethod = MethodSpec.methodBuilder("init")
                .addModifiers(Modifier.STATIC, Modifier.PUBLIC)
                .addJavadoc("集成模块的 ApiService 初始化方法\n")
                .returns(TypeName.VOID);
        for (StubFactoryGenerator subFactory : mStubFactoryGenerators) {
            ClassName stubFactoryType = ClassName.get(subFactory.packageName(), subFactory.className());

            initMethod.addStatement("$T.API_FACTORYS.put($S, new $T())", ApiService.class,
                    subFactory.mApiName, stubFactoryType);
        }
        builder.addMethod(initMethod.build());

        return builder.build();
    }
}
