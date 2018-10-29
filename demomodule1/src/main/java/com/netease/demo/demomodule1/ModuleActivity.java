package com.netease.demo.demomodule1;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.netease.demo.apiservice.api_service.AppCalculator;
import com.netease.demo.apiservice.api_service.AppDataModel;
import com.netease.demo.apiservice.api_service.AppSingleton;
import com.netease.demo.apiservice.api_service.factory.AppCalculatorApiFactory;
import com.netease.demo.apiservice.api_service.factory.AppDataModelApiFactory;
import com.netease.demo.apiservice.api_service.factory.AppSingletonApiFactory;
import com.netease.libs.apiservice.ApiService;

/**
 * Created by zyl06 on 2018/10/26.
 */

public class ModuleActivity extends Activity {

    private AppCalculator mCalculator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module);

        AppCalculatorApiFactory apiFactory = ApiService.getApiFactory("AppCalculator");
        mCalculator = apiFactory.newInstance();
    }

    public void onButton1Click(View v) {
        int result = mCalculator.add(1, 2);
        Toast.makeText(this, "Calculator add 1,2 is " + result, Toast.LENGTH_LONG).show();
    }

    public void onButton2Click(View v) {
        AppSingletonApiFactory apiFactory = ApiService.getApiFactory("AppSingleton");
        AppSingleton singleton = apiFactory.getInstance();
        String result = singleton.foo1("var1", "var2");
        Toast.makeText(this, "singleton foo1 var1, var2 is " + result, Toast.LENGTH_LONG).show();
    }

    public void onButton3Click(View v) {
        AppDataModelApiFactory apiFactory = ApiService.getApiFactory("AppDataModel");
        AppDataModel model = apiFactory.newInstance(4, 5);
        int result = mCalculator.add(model);
        Toast.makeText(this, "Calculator add DataModel(4, 5) is " + result, Toast.LENGTH_LONG).show();
    }

}
