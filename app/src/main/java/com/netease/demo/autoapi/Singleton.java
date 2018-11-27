package com.netease.demo.autoapi;

import android.util.Log;

import com.netease.libs.autoapi.anno.AutoApiClassAnno;
import com.netease.libs.autoapi.anno.AutoApiClassBuildMethodAnno;
import com.netease.libs.autoapi.anno.AutoApiMethodAnno;

/**
 * Created by zyl06 on 2018/10/28.
 */
@AutoApiClassAnno(name = "AppSingleton")
public class Singleton {

    private static Singleton sInstance = null;

    @AutoApiClassBuildMethodAnno()
    public static Singleton getInstance() {
        if (sInstance == null) {
            synchronized (Singleton.class) {
                if (sInstance == null) {
                    sInstance = new Singleton();
                }
            }
        }

        return sInstance;
    }

    private Singleton() {
    }

    @AutoApiMethodAnno()
    public String foo1(String str1, String str2) {
        Log.i("Singleton", "foo1 called");
        return str1 + "_" + str2;
    }

    @AutoApiMethodAnno()
    public void foo2() {
        Log.i("Singleton", "foo2 called");
    }

    @AutoApiMethodAnno()
    public void foo3() {
        Log.i("Singleton", "foo3 called ");
    }
}
