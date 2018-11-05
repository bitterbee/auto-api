package com.netease.demo.apiservice.http;

import android.util.Log;

import com.netease.libs.apiservice.anno.ApiServiceClassAnno;

/**
 * Created by zyl06 on 2018/11/5.
 */
@ApiServiceClassAnno(allPublicNormalApi = true)
public class Request {

    public void cancel() {
        Log.i("ApiService", "cancel request");
    }
}
