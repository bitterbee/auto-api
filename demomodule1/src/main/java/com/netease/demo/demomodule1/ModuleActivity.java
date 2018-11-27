package com.netease.demo.demomodule1;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.netease.demo.autoapi.auto_api.AddUtilApi;
import com.netease.demo.autoapi.auto_api.AppCalculator;
import com.netease.demo.autoapi.auto_api.AppDataModel;
import com.netease.demo.autoapi.auto_api.AppSingleton;
import com.netease.demo.autoapi.auto_api.CalculatorApi;
import com.netease.demo.autoapi.auto_api.DeviceUtilApi;
import com.netease.demo.autoapi.auto_api.EventAApi;
import com.netease.demo.autoapi.auto_api.HttpListenerApi;
import com.netease.demo.autoapi.auto_api.RequestApi;
import com.netease.demo.autoapi.auto_api.SharedWzpCommonRequestTaskApi;
import com.netease.demo.autoapi.auto_api.factory.AddUtilApiFactory;
import com.netease.demo.autoapi.auto_api.factory.AppCalculatorApiFactory;
import com.netease.demo.autoapi.auto_api.factory.AppDataModelApiFactory;
import com.netease.demo.autoapi.auto_api.factory.AppSingletonApiFactory;
import com.netease.demo.autoapi.auto_api.factory.CalculatorApiFactory;
import com.netease.demo.autoapi.auto_api.factory.DeviceUtilApiFactory;
import com.netease.demo.autoapi.auto_api.factory.EventAApiFactory;
import com.netease.demo.autoapi.auto_api.factory.SharedWzpCommonRequestTaskApiFactory;
import com.netease.libs.autoapi.AutoApi;

/**
 * Created by zyl06 on 2018/10/26.
 */
public class ModuleActivity extends Activity {

    private AppCalculator mCalculator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module);

        AppCalculatorApiFactory apiFactory = AutoApi.getApiFactory("AppCalculator");
        mCalculator = apiFactory.newInstance();

        DeviceUtilApiFactory deviceUtilFactory = AutoApi.getApiFactory("DeviceUtilApi");
        DeviceUtilApi deviceUtil = deviceUtilFactory.newInstance();
        int id = deviceUtil.getDeviceId();

        CalculatorApiFactory calculatorFactory = AutoApi.getApiFactory("CalculatorApi");
        CalculatorApi calculator = calculatorFactory.newInstance();
        int add = calculator.add(1,2); // 3
        int minuse = calculator.minuse(2, 1); // 1
        int multiply = calculator.multiply(2,3); //6
    }

    public void onButton1Click(View v) {

        AddUtilApiFactory factory = AutoApi.getApiFactory("AddUtilApi");
        AddUtilApi api = factory.newInstance(11, 12);
        int result0 = api.calu(); // 23
        int result1 = api.add(11,12); // 23

        Toast.makeText(this, "Calculator add 11,12 is " + api.calu(), Toast.LENGTH_LONG).show();
        Toast.makeText(this, "Calculator add 11,12 is " + api.add(11, 12), Toast.LENGTH_LONG).show();
    }

    public void onButton2Click(View v) {
        int result = mCalculator.add(1, 2);
        Toast.makeText(this, "Calculator add 1,2 is " + result, Toast.LENGTH_LONG).show();
    }

    public void onButton3Click(View v) {
        AppSingletonApiFactory apiFactory = AutoApi.getApiFactory("AppSingleton");
        AppSingleton singleton = apiFactory.getInstance();
        String result = singleton.foo1("var1", "var2");
        Toast.makeText(this, "singleton foo1 var1, var2 is " + result, Toast.LENGTH_LONG).show();
    }

    public void onButton4Click(View v) {
        AppDataModelApiFactory apiFactory = AutoApi.getApiFactory("AppDataModel");
        AppDataModel model = apiFactory.newInstance(4, 5);
        int result = mCalculator.add(model);
        Toast.makeText(this, "Calculator add DataModel(4, 5) is " + result, Toast.LENGTH_LONG).show();
    }

    public void onButton5Click(View v) {
        AppDataModelApiFactory apiFactory = AutoApi.getApiFactory("AppDataModel");
        AppDataModel model1 = apiFactory.newInstance(4, 5);
        AppDataModel model2 = apiFactory.newInstance(1, 2);
        AppDataModel result = mCalculator.add(model1, model2);
        Toast.makeText(this, String.format("Calculator add (4, 5) with (1, 2)  is (%d, %d)", result.getA(), result.getB()),
                Toast.LENGTH_LONG).show();
    }

    public void onButton6Click(View v) {
        SharedWzpCommonRequestTaskApiFactory apiFactory = AutoApi.getApiFactory("SharedWzpCommonRequestTaskApi");
        SharedWzpCommonRequestTaskApi api = apiFactory.newInstance("/xhr/test/a", null, null, null, null);
        RequestApi request = api.query(new HttpListenerApi() {
            @Override
            public void onCancel() {  }

            @Override
            public void onHttpSuccess(String httpName, Object result) {  }

            @Override
            public void onHttpError(String httpName, int errorCode, String errorMsg) {  }

            @Override
            public Object getApiServiceTarget() {
                return null;
            }
        });

        // 取消请求
        request.cancel();
    }

    public void onButton7Click(View v) {
        EventAApiFactory factory = AutoApi.getApiFactory("EventAApi");
        EventAApi api = factory.newInstance();
//        EventBus.getDefault().post(api.getApiServiceTarget());
    }
}
