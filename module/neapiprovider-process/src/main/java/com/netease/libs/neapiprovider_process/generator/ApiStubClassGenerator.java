package com.netease.libs.neapiprovider_process.generator;

import com.netease.libs.neapiprovider_process.ElementUtil;
import com.netease.libs.neapiprovider_process.NEApiProviderClass;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.List;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import static com.squareup.javapoet.TypeSpec.classBuilder;
import static javax.lang.model.element.Modifier.PUBLIC;

/**
 * Created by zyl06 on 2018/10/18.
 */

public class ApiStubClassGenerator extends BaseApiClassGenerator {

    ApiInterfaceGenerator mApiGenerator;

    public ApiStubClassGenerator(NEApiProviderClass providerClass, Messager messager,
                                 ApiInterfaceGenerator apiGenerator,
                                 String packageName) {
        super(providerClass, messager, packageName);
        this.mApiGenerator = apiGenerator;
    }

    @Override
    public String packageName() {
        return (mPkgName == null || mPkgName.isEmpty()) ?
                "com.netease.libs.api_impl" :
                mPkgName + ".api_impl";
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

//        FieldSpec.Builder target = FieldSpec.builder(mApiTarget.getClass(), "target",
//                Modifier.PRIVATE, Modifier.STATIC);

        List<? extends Element> allElements = mProviderClass.clazz.getEnclosedElements();
        for (Element elem : allElements) {
            if (elem instanceof ExecutableElement &&
                    ElementUtil.isPublic(elem) &&
                    ElementUtil.isStatic(elem)) {
                addMethod(builder, (ExecutableElement) elem);
            }
        }

        return builder.build();
    }

    private void addMethod(TypeSpec.Builder builder, ExecutableElement e) {
        MethodSpec.Builder methodBuilder = MethodSpec
                .methodBuilder(e.getSimpleName().toString())
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

        builder.addMethod(methodBuilder.build());
    }
}
