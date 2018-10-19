package com.netease.libs.neapiprovider_process;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import static com.squareup.javapoet.TypeSpec.interfaceBuilder;
import static javax.lang.model.element.Modifier.PUBLIC;

/**
 * Created by zyl06 on 2018/10/17.
 */

public class ApiInterfaceGenerator extends BaseApiClassGenerator {

    private String mFromPath;
    private String mToPath;

    public ApiInterfaceGenerator(NEApiProviderClass providerClass, Messager messager, String fromPath, String toPath) {
        super(providerClass, messager);
        this.mFromPath = fromPath;
        this.mToPath = toPath;
    }

    @Override
    public String packageName() {
        return "com.netease.libs.apiprovider";
    }

    @Override
    public String className() {
        return mProviderClass.name != null && !mProviderClass.name.equals("") ?
                mProviderClass.name + "Api":
                mApiTarget.getSimpleName() + "Api";
    }

    @Override
    public TypeSpec generate() {
        TypeSpec.Builder builder = interfaceBuilder(className())
                .addModifiers(PUBLIC);
        builder.addJavadoc(mApiTarget.getQualifiedName().toString() + " 的 api 接口\n");

        List<? extends Element> allElements = mApiTarget.getEnclosedElements();
        for (Element elem : allElements) {
            if (elem instanceof ExecutableElement &&
                    ElementUtil.isPublic(elem) &&
                    ElementUtil.isStatic(elem)) {
                addMethod(builder, (ExecutableElement) elem);
            }
        }

        return builder.build();
    }

    @Override
    public void writeTo(JavaFile javaFile, Filer filer) throws IOException {
        String to = mToPath + "/src/api/java/";
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

    private void addMethod(TypeSpec.Builder builder, ExecutableElement e) {
        MethodSpec.Builder methodBuilder = MethodSpec
                .methodBuilder(e.getSimpleName().toString())
                .returns(TypeName.get(e.getReturnType()));
        for (VariableElement param : e.getParameters()) {
            methodBuilder.addParameter(TypeName.get(param.asType()), param.getSimpleName().toString());
        }
        for (TypeMirror throwType : e.getThrownTypes()) {
            methodBuilder.addException(TypeName.get(throwType));
        }
        methodBuilder.addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT);

        builder.addMethod(methodBuilder.build());
    }
}
