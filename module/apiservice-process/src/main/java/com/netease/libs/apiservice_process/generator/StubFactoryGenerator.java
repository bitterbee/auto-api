package com.netease.libs.apiservice_process.generator;

import com.netease.libs.apiservice_process.ApiServiceClass;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.Messager;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;

import static com.squareup.javapoet.TypeSpec.classBuilder;
import static javax.lang.model.element.Modifier.PUBLIC;

/**
 * Created by zyl06 on 2018/10/27.
 */

public class StubFactoryGenerator  extends BaseApiClassGenerator {

    private ClassName mReturnType;
    private ClassName mStubType;
    ClassName mSuperType;
    String mApiName;
    private int mFactoryMethodCount = 0;

    public StubFactoryGenerator(ApiServiceClass providerClass, Messager messager, String packageName,
                                ApiGenerator apiGen, StubClassGenerator stubClassGen, ApiFactoryGenerator apiFactoryGen) {
        super(providerClass, messager, packageName);
        this.mReturnType = ClassName.get(apiGen.packageName(), apiGen.className());
        this.mStubType = ClassName.get(stubClassGen.packageName(), stubClassGen.className());
        this.mSuperType = ClassName.get(apiFactoryGen.packageName(), apiFactoryGen.className());
        this.mApiName = apiGen.className();
    }

    @Override
    public String packageName() {
        return (mPkgName == null || mPkgName.isEmpty()) ?
                "com.netease.libs.api_service.factory" :
                mPkgName + ".api_service.factory";
    }

    @Override
    public String className() {
        return className("StubFactory", "StubFactory");
    }

    @Override
    public TypeSpec generate() {
        TypeSpec.Builder builder = classBuilder(className())
                .addModifiers(PUBLIC)
                .addSuperinterface(mSuperType);
        builder.addJavadoc(mApiTarget.getQualifiedName().toString() + " stub 类的工厂接口\n");

        generate(builder);

        if (mFactoryMethodCount == 0) {
            addFactoryMethod(builder, null, "newInstance");
        }

        return builder.build();
    }

    @Override
    protected void addFactoryMethod(TypeSpec.Builder builder, ExecutableElement e, String methodName) {
        mFactoryMethodCount ++;

        MethodSpec.Builder methodBuilder = MethodSpec
                .methodBuilder(methodName)
                .returns(mReturnType)
                .addModifiers(Modifier.PUBLIC);
        if (e != null) {
            for (VariableElement param : e.getParameters()) {
                methodBuilder.addParameter(TypeName.get(param.asType()), param.getSimpleName().toString());
            }
        }

        methodBuilder.addStatement("$T stub = new $T()", mStubType, mStubType);

        if (e != null) {
            StringBuilder sb = new StringBuilder(64);
            sb.append("stub.mTarget = new $T(");

            int count = e.getParameters().size();
            for (int i=0; i<count; i++) {
                VariableElement param = e.getParameters().get(i);
                sb.append(param.getSimpleName().toString());
                if (i != count - 1) {
                    sb.append(", ");
                }
            }

            sb.append(")");
            methodBuilder.addStatement(sb.toString(), TypeName.get(mApiTarget.asType()));
        }

        methodBuilder.addStatement("return stub");

        builder.addMethod(methodBuilder.build());
    }

    @Override
    protected void addClassBuildMethod(TypeSpec.Builder builder, ExecutableElement e, String methodName) {
        TypeName returnType = ClassName.get(e.getReturnType());
        if (!returnType.equals(ClassName.get(mApiTarget))) {
            printError(mApiTarget.toString() + "." + methodName + " return type is not " + mApiTarget.toString());
            return;
        }

        mFactoryMethodCount ++;

        MethodSpec.Builder methodBuilder = MethodSpec
                .methodBuilder(methodName)
                .returns(mReturnType)
                .addModifiers(Modifier.PUBLIC);
        if (e != null) {
            for (VariableElement param : e.getParameters()) {
                methodBuilder.addParameter(TypeName.get(param.asType()), param.getSimpleName().toString());
            }
        }

        methodBuilder.addStatement("$T stub = new $T()", mStubType, mStubType);

        if (e != null) {
            StringBuilder sb = new StringBuilder(64);
            sb.append("stub.mTarget = $T.")
                    .append(e.getSimpleName())
                    .append("(");

            int count = e.getParameters().size();
            for (int i=0; i<count; i++) {
                VariableElement param = e.getParameters().get(i);
                sb.append(param.getSimpleName().toString());
                if (i != count - 1) {
                    sb.append(", ");
                }
            }

            sb.append(")");
            methodBuilder.addStatement(sb.toString(), TypeName.get(mApiTarget.asType()));
        }

        methodBuilder.addStatement("return stub");

        builder.addMethod(methodBuilder.build());

    }
}
