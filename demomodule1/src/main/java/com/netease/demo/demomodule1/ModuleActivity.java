package com.netease.demo.demomodule1;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.netease.demo.autoapi.auto_api.AddUtilApi;
import com.netease.demo.autoapi.auto_api.AppCalculator;
import com.netease.demo.autoapi.auto_api.CalculatorAlias;
import com.netease.demo.autoapi.auto_api.CalculatorApi;
import com.netease.demo.autoapi.auto_api.DataModelApi;
import com.netease.demo.autoapi.auto_api.DataModelOperatorApi;
import com.netease.demo.autoapi.auto_api.DeviceUtilApi;
import com.netease.demo.autoapi.auto_api.EventAApi;
import com.netease.demo.autoapi.auto_api.HttpListenerApi;
import com.netease.demo.autoapi.auto_api.HttpUtilApi;
import com.netease.demo.autoapi.auto_api.MinusApi;
import com.netease.demo.autoapi.auto_api.RequestApi;
import com.netease.demo.autoapi.auto_api.SharedWzpCommonRequestTaskApi;
import com.netease.demo.autoapi.auto_api.SingletonApi;
import com.netease.demo.autoapi.auto_api.factory.AddUtilApiFactory;
import com.netease.demo.autoapi.auto_api.factory.AppCalculatorApiFactory;
import com.netease.demo.autoapi.auto_api.factory.CalculatorAliasApiFactory;
import com.netease.demo.autoapi.auto_api.factory.CalculatorApiFactory;
import com.netease.demo.autoapi.auto_api.factory.DataModelApiFactory;
import com.netease.demo.autoapi.auto_api.factory.DataModelOperatorApiFactory;
import com.netease.demo.autoapi.auto_api.factory.DeviceUtilApiFactory;
import com.netease.demo.autoapi.auto_api.factory.EventAApiFactory;
import com.netease.demo.autoapi.auto_api.factory.HttpUtilApiFactory;
import com.netease.demo.autoapi.auto_api.factory.MinusApiFactory;
import com.netease.demo.autoapi.auto_api.factory.SharedWzpCommonRequestTaskApiFactory;
import com.netease.demo.autoapi.auto_api.factory.SingletonApiFactory;
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

        CalculatorAliasApiFactory calculatorFactory = AutoApi.getApiFactory("CalculatorAlias");
        CalculatorAlias calculator = calculatorFactory.newInstance();
        int add = calculator.add(1,2); // 3
        int minuse = calculator.minuse(2, 1); // 1
        int multiply = calculator.multiply(2,3); //6
    }

    public void onButton0Click(View v) {

        MinusApiFactory factory = AutoApi.getApiFactory("MinusApi");
        MinusApi api = factory.newInstance(10, 5);
        int result0 = api.calu(); // 5
        int result1 = api.minus(10, 5); // 5
    }

    public void onButton1Click(View v) {

        AddUtilApiFactory factory = AutoApi.getApiFactory("AddUtilApi");
        AddUtilApi api0 = factory.newInstance(11, 12);
        AddUtilApi api1 = factory.newInstance(3);
        AddUtilApi api2 = factory.getInstance(1, 2);
        int result0 = api0.calu(); // 23
        int result1 = api1.calu(); // 6
        int result2 = api1.calu(); // 3

//        AddUtilApiFactory factory = AutoApi.getApiFactory("AddUtilApi");
//        AddUtilApi api = factory.newInstance(11, 12);
//        int result0 = api.calu(); // 23
//        int result1 = api.add(11,12); // 23

//        Toast.makeText(this, "Calculator add 11,12 is " + api.calu(), Toast.LENGTH_LONG).show();
//        Toast.makeText(this, "Calculator add 11,12 is " + api.add(11, 12), Toast.LENGTH_LONG).show();
    }

    public void onButton2Click(View v) {
        int result = mCalculator.add(1, 2);
        Toast.makeText(this, "Calculator add 1,2 is " + result, Toast.LENGTH_LONG).show();
    }

    public void onButton3Click(View v) {
        SingletonApiFactory apiFactory = AutoApi.getApiFactory("SingletonApi");
        SingletonApi singleton = apiFactory.getInstance();
        String result = singleton.foo1("var1", "var2");
        Toast.makeText(this, "singleton foo1 var1, var2 is " + result, Toast.LENGTH_LONG).show();
    }

    public void onButton4Click(View v) {
        DataModelApiFactory apiFactory = AutoApi.getApiFactory("DataModelApi");
        DataModelApi model = apiFactory.newInstance(4, 5);
        int result = mCalculator.add(model);
        Toast.makeText(this, "Calculator add DataModel(4, 5) is " + result, Toast.LENGTH_LONG).show();
    }

    public void onButton5Click(View v) {
        DataModelOperatorApiFactory factory = AutoApi.getApiFactory("DataModelOperatorApi");
        DataModelOperatorApi api = factory.newInstance();

        DataModelApiFactory dataFactory = AutoApi.getApiFactory("DataModelApi");
        DataModelApi model1 = dataFactory.newInstance(4, 5);
        DataModelApi model2 = dataFactory.newInstance(1, 2);
        DataModelApi result = api.add(model1, model2);
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

    public void onButton8Click(View v) {
        HttpUtilApiFactory factory = AutoApi.getApiFactory("HttpUtilApi");
        HttpUtilApi api = factory.newInstance();
        RequestApi request = api.query(new HttpListenerApi() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onHttpSuccess(String httpName, Object result) {

            }

            @Override
            public void onHttpError(String httpName, int errorCode, String errorMsg) {

            }

            @Override
            public Object getApiServiceTarget() {
                return null;
            }
        });

        // 根据业务场景执行请求取消操作
        request.cancel();
    }
}
