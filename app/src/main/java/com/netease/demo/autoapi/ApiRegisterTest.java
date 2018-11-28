package com.netease.demo.autoapi;

import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;

/**
 * Created by zyl06 on 2018/11/28.
 */
@Aspect
public class ApiRegisterTest {

    public static boolean isInit = false;

    @After("execution(void com.netease.libs.AutoApi.init())")
    public void init(JoinPoint joinPoint) {
        isInit = true;
        Log.e("TEST", "ApiRegisterTest init");
    }
}
