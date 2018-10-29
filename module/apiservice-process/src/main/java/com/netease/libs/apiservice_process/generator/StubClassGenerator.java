package com.netease.libs.apiservice_process.generator;

import com.netease.libs.apiservice_process.ApiServiceClass;
import com.netease.libs.apiservice_process.ElementUtil;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.List;

import javax.annotation.processing.Messager;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import static com.squareup.javapoet.TypeSpec.classBuilder;
import static javax.lang.model.element.Modifier.PUBLIC;

/**
 * Created by zyl06 on 2018/10/18.
 */

public class StubClassGenerator extends BaseApiClassGenerator {

    ApiGenerator mApiGenerator;
    private boolean mMakeTargetField = false;

    public StubClassGenerator(ApiServiceClass providerClass, Messager messager,
                              ApiGenerator apiGenerator,
                              String packageName) {
        super(providerClass, messager, packageName);
        this.mApiGenerator = apiGenerator;
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
                mProviderClass.name + "Stub" :
                mApiTarget.getSimpleName() + "Stub";
    }

    @Override
    public TypeSpec generate() {

        TypeSpec.Builder builder = classBuilder(className())
                .addSuperinterface(ClassName.get(mApiGenerator.packageName(), mApiGenerator.className()))
                .addModifiers(PUBLIC);
        builder.addJavadoc(mApiTarget.getQualifiedName().toString() + " 的 stub class\n");

        generate(builder);

        return builder.build();
    }

    // 在 addMethod 之后执行
    @Override
    protected void addCustomFields(TypeSpec.Builder builder) {
        // 生成成员变量
        if (mMakeTargetField) {
            FieldSpec.Builder targetBuild = FieldSpec.builder(TypeName.get(mApiTarget.asType()), "mTarget",
                    Modifier.PUBLIC, Modifier.STATIC);
            FieldSpec target = targetBuild.addJavadoc("非静态方法执行真正的对象\n").build();
            builder.addField(target);
        }
    }

    @Override
    protected void addMethod(TypeSpec.Builder builder, ExecutableElement e, String methodName) {
        MethodSpec.Builder methodBuilder = MethodSpec
                .methodBuilder(methodName)
                .returns(TypeName.get(e.getReturnType()));

        List<? extends VariableElement> params = e.getParameters();
        for (VariableElement param : params) {
            methodBuilder.addParameter(TypeName.get(param.asType()), param.getSimpleName().toString());
        }
        for (TypeMirror throwType : e.getThrownTypes()) {
            methodBuilder.addException(TypeName.get(throwType));
        }
        methodBuilder.addModifiers(Modifier.PUBLIC);

        StringBuilder sb = new StringBuilder(32);

        if (ElementUtil.isStatic(e)) {
            // 静态方法
            if (e.getReturnType() != null && TypeName.get(e.getReturnType()) != TypeName.VOID) {
                sb.append("return ");
            }
            sb.append("$T.")
                    .append(e.getSimpleName().toString())
                    .append("(");

            int paramCount = params.size();
            for (int i = 0; i < paramCount; i++) {
                sb.append(params.get(i).getSimpleName().toString());
                if (i < paramCount - 1) {
                    sb.append(",");
                }
            }
            sb.append(")");

            methodBuilder.addStatement(sb.toString(), mApiTarget);
        } else {
            mMakeTargetField = true;

            // 非静态方法
            if (e.getReturnType() != null && TypeName.get(e.getReturnType()) != TypeName.VOID) {
                sb.append("return ");
            }
            sb.append("mTarget.")
                    .append(e.getSimpleName().toString())
                    .append("(");

            int paramCount = params.size();
            for (int i = 0; i < paramCount; i++) {
                sb.append(params.get(i).getSimpleName().toString());
                if (i < paramCount - 1) {
                    sb.append(",");
                }
            }
            sb.append(")");
            methodBuilder.addStatement(sb.toString());
        }

        builder.addMethod(methodBuilder.build());
    }
}
