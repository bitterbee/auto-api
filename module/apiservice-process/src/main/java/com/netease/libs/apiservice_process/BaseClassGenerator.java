package com.netease.libs.apiservice_process;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * Created by zyl06 on 2018/10/17.
 */

public abstract class BaseClassGenerator {

    protected Messager mMessager;

    public BaseClassGenerator(Messager messager) {
        this.mMessager = messager;
    }

    public abstract TypeSpec generate();

    public void writeTo(JavaFile javaFile, Filer filer) throws IOException {
        javaFile.writeTo(filer);
    }

    public abstract String packageName();

    public abstract String className();

    public void printError(String msg) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, msg);
    }

    protected String getClassName(Element element) {
        ClassName className = ClassName.bestGuess(element.toString());

        // get class full name
        StringBuilder sbClazzName = new StringBuilder(32);
        sbClazzName.append(className.packageName()).append(".");
        for (String simplename : className.simpleNames()) {
            sbClazzName.append(simplename);
            sbClazzName.append("$");
        }
        sbClazzName.deleteCharAt(sbClazzName.length() - 1);

        return sbClazzName.toString();
    }

    protected Class primaryTypeGuess(String typeName) {
        if ("int".equals(typeName)) {
            return int.class;
        } else if ("long".equals(typeName)) {
            return long.class;
        } else if ("float".equals(typeName)) {
            return float.class;
        } else if ("boolean".equals(typeName)) {
            return boolean.class;
        } else if ("double".equals(typeName)) {
            return double.class;
        } else if ("char".equals(typeName)) {
            return char.class;
        } else if ("short".equals(typeName)) {
            return short.class;
        } else if ("byte".equals(typeName)) {
            return byte.class;
        }

        return null;
    }
}
