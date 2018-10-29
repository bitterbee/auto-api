package com.netease.demo.apiservice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.netease.demo.demomodule1.ModuleActivity;
import com.netease.demo.demomodule1.api_service.ModuleFunction;
import com.netease.demo.demomodule1.api_service.factory.ModuleFunctionApiFactory;
import com.netease.libs.apiservice.ApiService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onButton0Click(View v) {
        ModuleFunctionApiFactory factory = ApiService.getApiFactory("ModuleFunction");
        ModuleFunction moduleFunction = factory.newInstance();
        int result = moduleFunction.multiple(2, 3);
        Toast.makeText(this, "multiple 2, 3 is " + result, Toast.LENGTH_LONG).show();
    }

    public void onButton1Click(View v) {
        Intent i = new Intent(this, ModuleActivity.class);
        startActivity(i);
    }
}