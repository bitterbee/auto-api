package com.netease.demo.demomodule1;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.netease.demo.apiservice.api.AppCalculatorApiFactory;
import com.netease.demo.apiservice.api.AppSingletonApiFactory;
import com.netease.demo.apiservice.api_service.AppCalculator;
import com.netease.demo.apiservice.api_service.AppSingleton;
import com.netease.libs.apiservice.ApiService;

/**
 * Created by zyl06 on 2018/10/26.
 */

public class ModuleActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module);
    }

    public void onButton1Click(View v) {
        AppCalculatorApiFactory apiFactory = ApiService.getApiFactory("AppCalculator");
        AppCalculator calculator = apiFactory.newInstance();
        int result = calculator.add(1, 2);
        Toast.makeText(this, "calculator add 1,2 is " + result, Toast.LENGTH_LONG).show();
    }

    public void onButton2Click(View v) {
        AppSingletonApiFactory apiFactory = ApiService.getApiFactory("AppSingleton");
        AppSingleton singleton = apiFactory.getInstance();
        String result = singleton.foo1("var1", "var2");
        Toast.makeText(this, "singleton foo1 var1, var2 is " + result, Toast.LENGTH_LONG).show();
    }
}
