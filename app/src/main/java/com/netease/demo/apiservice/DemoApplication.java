package com.netease.demo.apiservice;

import android.app.Application;

import com.netease.demo.apiservice.api_service.ApiRegister;

/**
 * Created by zyl06 on 2018/7/30.
 */

public class DemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ApiRegister.init();
    }
}
