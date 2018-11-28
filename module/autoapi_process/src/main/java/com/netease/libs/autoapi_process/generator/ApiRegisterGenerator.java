package com.netease.libs.autoapi_process.generator;

import com.netease.libs.autoapi.AutoApi;
import com.netease.libs.autoapi_process.BaseClassGenerator;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;

import static com.squareup.javapoet.TypeSpec.classBuilder;
import static javax.lang.model.element.Modifier.PUBLIC;

/**
 * Created by zyl06 on 2018/10/19.
 */

public class ApiRegisterGenerator extends BaseClassGenerator {

    private String mPkgName;
    private List<StubFactoryGenerator> mStubFactoryGenerators = new ArrayList<>();

    public ApiRegisterGenerator(String pkgName,
                                List<StubFactoryGenerator> stubFactoryGenerators) {
        super();
        mPkgName = pkgName;
        mStubFactoryGenerators = stubFactoryGenerators;
    }

    @Override
    public String packageName() {
        return mPkgName + ".auto_api";
    }

    @Override
    public String className() {
        return "ApiRegister";
    }

    @Override
    public TypeSpec generate() {
        TypeSpec.Builder builder = classBuilder(className())
                .addModifiers(PUBLIC)
                .addAnnotation(ClassName.get("org.aspectj.lang.annotation", "Aspect"));
        builder.addJavadoc("ApiService Register Class\n");

        MethodSpec.Builder initMethod = MethodSpec.methodBuilder("init")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ClassName.get("org.aspectj.lang", "JoinPoint"), "joinPoint")
                .addJavadoc("Collect & Register All StubFactories\n")
                .returns(TypeName.VOID);

        AnnotationSpec.Builder annoBuilder = AnnotationSpec.builder(ClassName.get("org.aspectj.lang.annotation", "After"));
        annoBuilder.addMember("value", "\"execution(void com.netease.libs.autoapi.AutoApi.init())\"");
        initMethod.addAnnotation(annoBuilder.build());

        for (StubFactoryGenerator subFactory : mStubFactoryGenerators) {
            ClassName stubFactoryType = ClassName.get(subFactory.packageName(), subFactory.className());

            initMethod.addStatement("$T.API_FACTORYS.put($S, new $T())", AutoApi.class,
                    subFactory.mApiName, stubFactoryType);
        }
        builder.addMethod(initMethod.build());

        return builder.build();
    }
}
