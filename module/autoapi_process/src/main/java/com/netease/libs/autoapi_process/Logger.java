package com.netease.libs.autoapi_process;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

/**
 * Created by zyl06 on 2018/11/8.
 */

public class Logger {

    static Messager sMessager;
    static boolean sLogOpen = false;

    public static void i(String msg) {
        if (sMessager != null && sLogOpen) {
            sMessager.printMessage(Diagnostic.Kind.NOTE, msg);
        }
    }

    public static void w(String msg) {
        if (sMessager != null && sLogOpen) {
            sMessager.printMessage(Diagnostic.Kind.WARNING, msg);
        }
    }

    public static void e(String msg) {
        if (sMessager != null && sLogOpen) {
            sMessager.printMessage(Diagnostic.Kind.ERROR, msg);
        }
    }

    public static void e(String msg, Element element) {
        if (sMessager != null && sLogOpen) {
            sMessager.printMessage(Diagnostic.Kind.ERROR, msg, element);
        }
    }
}
