package com.netease.demo.apiservice;

import android.util.Log;

import com.netease.libs.apiservice.anno.ApiServiceClassAnno;
import com.netease.libs.apiservice.anno.ApiServiceClassBuildMethodAnno;
import com.netease.libs.apiservice.anno.ApiServiceMethodAnno;

/**
 * Created by zyl06 on 2018/10/28.
 */
@ApiServiceClassAnno(name = "AppSingleton")
public class Singleton {

    private static Singleton sInstance = null;

    @ApiServiceClassBuildMethodAnno()
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

    @ApiServiceMethodAnno()
    public String foo1(String str1, String str2) {
        Log.i("Singleton", "foo1 called");
        return str1 + "_" + str2;
    }

    @ApiServiceMethodAnno()
    public void foo2() {
        Log.i("Singleton", "foo2 called");
    }
}
