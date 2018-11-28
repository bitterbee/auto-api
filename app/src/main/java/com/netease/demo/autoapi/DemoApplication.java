package com.netease.demo.autoapi;

import android.app.Application;

import com.netease.libs.autoapi.AutoApi;

/**
 * Created by zyl06 on 2018/7/30.
 */

public class DemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AutoApi.init();
    }
}
